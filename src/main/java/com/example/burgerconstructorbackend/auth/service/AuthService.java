package com.example.burgerconstructorbackend.auth.service;

import com.example.burgerconstructorbackend.auth.dto.*;
import com.example.burgerconstructorbackend.auth.exception.BadRequestException;
import com.example.burgerconstructorbackend.auth.exception.NotFoundException;
import com.example.burgerconstructorbackend.auth.exception.UnauthorizedException;
import com.example.burgerconstructorbackend.auth.refresh.RefreshToken;
import com.example.burgerconstructorbackend.auth.refresh.RefreshTokenRepository;
import com.example.burgerconstructorbackend.security.JwtService;
import com.example.burgerconstructorbackend.user.dto.UserResponseDto;
import com.example.burgerconstructorbackend.user.entity.User;
import com.example.burgerconstructorbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserWrapperResponse getUser(Authentication authentication) {
        User user = requireUser(authentication);
        return new UserWrapperResponse(true, new UserResponseDto(user.getEmail(), user.getName()));
    }

    public UserWrapperResponse updateUser(UpdateUserRequest request, Authentication authentication) {
        User user = requireUser(authentication);

        if (request.email() != null && !request.email().isBlank()) {
            String newEmail = request.email().trim();

            if (!newEmail.equalsIgnoreCase(user.getEmail())
                    && userRepository.existsByEmail(newEmail)) {
                throw new BadRequestException("Email already exists");
            }
            user.setEmail(newEmail);
        }

        if (request.name() != null && !request.name().isBlank()) {
            user.setName(request.name().trim());
        }

        if (request.password() != null && !request.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
            // опционально: сбросить refresh после смены пароля
            refreshTokenRepository.deleteByUser(user);
        }

        userRepository.save(user);

        return new UserWrapperResponse(true, new UserResponseDto(user.getEmail(), user.getName()));
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setName(request.name());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveRefreshToken(user, refreshToken);

        return new AuthResponse(
                true,
                refreshToken,
                accessToken,
                new UserResponseDto(user.getEmail(), user.getName())
        );
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid password");
        }

        refreshTokenRepository.deleteByUser(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveRefreshToken(user, refreshToken);

        return new AuthResponse(
                true,
                refreshToken,
                accessToken,
                new UserResponseDto(user.getEmail(), user.getName())
        );
    }

    public AuthResponse refresh(TokenRequest request) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(request.token())
                .orElseThrow(() -> new UnauthorizedException("Token not found"));

        if (storedToken.getExpiresAt().isBefore(Instant.now())) {
            refreshTokenRepository.delete(storedToken);
            throw new UnauthorizedException("Token expired");
        }

        User user = storedToken.getUser();

        refreshTokenRepository.delete(storedToken);

        String newAccess = jwtService.generateAccessToken(user);
        String newRefresh = jwtService.generateRefreshToken(user);

        saveRefreshToken(user, newRefresh);

        return new AuthResponse(true, newRefresh, newAccess, null);
    }

    public SuccessResponse logout(TokenRequest request, Authentication authentication) {
        User user = requireUser(authentication);

        refreshTokenRepository.findByToken(request.token())
                .filter(token -> token.getUser().equals(user))
                .ifPresent(refreshTokenRepository::delete);

        return new SuccessResponse(true);
    }

    private User requireUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Unauthorized");
        }
        return (User) authentication.getPrincipal();
    }

    private void saveRefreshToken(User user, String refreshToken) {
        RefreshToken entity = new RefreshToken();
        entity.setToken(refreshToken);
        entity.setUser(user);
        entity.setExpiresAt(Instant.now().plusMillis(jwtService.getRefreshExpiration()));
        refreshTokenRepository.save(entity);
    }
}
