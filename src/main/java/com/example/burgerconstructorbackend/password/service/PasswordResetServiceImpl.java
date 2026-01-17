package com.example.burgerconstructorbackend.password.service;

import com.example.burgerconstructorbackend.password.dto.PasswordResetConfirmRequest;
import com.example.burgerconstructorbackend.password.dto.PasswordResetRequest;
import com.example.burgerconstructorbackend.password.dto.SuccessResponse;
import com.example.burgerconstructorbackend.password.entity.PasswordResetToken;
import com.example.burgerconstructorbackend.password.repository.PasswordResetTokenRepository;
import com.example.burgerconstructorbackend.user.entity.User;
import com.example.burgerconstructorbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Duration TTL = Duration.ofMinutes(30);

    @Override
    @Transactional
    public SuccessResponse requestReset(PasswordResetRequest request) {
        if (request == null || request.email() == null || request.email().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email must not be blank");
        }

        // ВАЖНО: по безопасности лучше ВСЕГДА возвращать success:true,
        // даже если email не найден (чтобы не палить существование аккаунта)
        userRepository.findByEmail(request.email()).ifPresent(user -> {
            // удалим старые токены пользователя (чтобы был один активный)
            tokenRepository.deleteByUserId(user.getId());

            String token = UUID.randomUUID() + "-" + UUID.randomUUID(); // простой, но норм
            PasswordResetToken prt = new PasswordResetToken();
            prt.setToken(token);
            prt.setUser(user);
            prt.setExpiresAt(LocalDateTime.now().plus(TTL));

            tokenRepository.save(prt);

            // тут обычно отправка email, пока — лог:
            System.out.println("PASSWORD RESET TOKEN for " + user.getEmail() + ": " + token);
        });

        return new SuccessResponse(true);
    }

    @Override
    @Transactional
    public SuccessResponse reset(PasswordResetConfirmRequest request) {
        if (request == null || request.token() == null || request.token().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "token must not be blank");
        }
        if (request.password() == null || request.password().length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password must be at least 6 chars");
        }

        PasswordResetToken prt = tokenRepository.findByToken(request.token())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid token"));

        if (prt.getExpiresAt().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(prt);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "token expired");
        }

        User user = prt.getUser();
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        // токен одноразовый
        tokenRepository.delete(prt);

        return new SuccessResponse(true);
    }
}
