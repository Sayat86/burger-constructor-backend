package com.example.burgerconstructorbackend.auth;

import com.example.burgerconstructorbackend.auth.dto.AuthResponse;
import com.example.burgerconstructorbackend.auth.dto.LoginRequest;
import com.example.burgerconstructorbackend.auth.dto.RegisterRequest;
import com.example.burgerconstructorbackend.auth.dto.TokenRequest;
import com.example.burgerconstructorbackend.auth.refresh_token.RefreshToken;
import com.example.burgerconstructorbackend.auth.refresh_token.RefreshTokenRepository;
import com.example.burgerconstructorbackend.security.JwtService;
import com.example.burgerconstructorbackend.user.User;
import com.example.burgerconstructorbackend.user.UserRepository;
import com.example.burgerconstructorbackend.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // -------------------- GET USER --------------------
    @GetMapping("/user")
    public Map<String, Object> getUser(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return Map.of(
                "success", true,
                "user", new UserDto(user.getId(), user.getEmail(), user.getName())
        );
    }

    // -------------------- REFRESH TOKEN --------------------
    @PostMapping("/token")
    public AuthResponse refresh(@RequestBody TokenRequest request) {

        String refreshToken = request.token();

        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token not found"));

        if (storedToken.getExpiresAt().isBefore(Instant.now())) {
            refreshTokenRepository.delete(storedToken);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
        }

        User user = storedToken.getUser();

        // Удаляем старый refresh
        refreshTokenRepository.delete(storedToken);

        // Генерируем новые токены
        String newAccess = jwtService.generateAccessToken(user);
        String newRefresh = jwtService.generateRefreshToken(user);

        RefreshToken newEntity = new RefreshToken();
        newEntity.setId(UUID.randomUUID());
        newEntity.setToken(newRefresh);
        newEntity.setUser(user);
        newEntity.setExpiresAt(Instant.now().plusSeconds(86400)); // 1 день
        refreshTokenRepository.save(newEntity);

        return new AuthResponse(true, newRefresh, newAccess, null);
    }

    // -------------------- REGISTER --------------------
    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(request.email());
        user.setName(request.name());
        user.setPasswordHash(passwordEncoder.encode(request.password()));

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        RefreshToken refreshEntity = new RefreshToken();
        refreshEntity.setId(UUID.randomUUID());
        refreshEntity.setToken(refreshToken);
        refreshEntity.setUser(user);
        refreshEntity.setExpiresAt(Instant.now().plusSeconds(86400));
        refreshTokenRepository.save(refreshEntity);

        return new AuthResponse(
                true,
                refreshToken,
                accessToken,
                new UserDto(user.getId(), user.getEmail(), user.getName())
        );
    }

    // -------------------- LOGIN --------------------
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        RefreshToken refreshEntity = new RefreshToken();
        refreshEntity.setId(UUID.randomUUID());
        refreshEntity.setToken(refreshToken);
        refreshEntity.setUser(user);
        refreshEntity.setExpiresAt(Instant.now().plusSeconds(86400));
        refreshTokenRepository.save(refreshEntity);

        return new AuthResponse(
                true,
                refreshToken,
                accessToken,
                new UserDto(user.getId(), user.getEmail(), user.getName())
        );
    }

    // -------------------- LOGOUT --------------------
    @PostMapping("/logout")
    public Map<String, Object> logout(@RequestBody TokenRequest request) {

        String refreshToken = request.token();

        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);

        return Map.of("success", true);
    }
}

