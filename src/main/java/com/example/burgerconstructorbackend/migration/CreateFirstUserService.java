package com.example.burgerconstructorbackend.migration;

import com.example.burgerconstructorbackend.auth.dto.RegisterRequest;
import com.example.burgerconstructorbackend.auth.service.AuthService;
import com.example.burgerconstructorbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateFirstUserService implements CommandLineRunner {

    private final AuthService authService;
    private final UserRepository userRepository;

    @Override
    public void run(String ... args) {
        RegisterRequest request = new RegisterRequest(
                "admin@example.com",
                "admin",
                "admin1234"
        );
        if (userRepository.existsByEmail(request.email())) {
            log.info("ADMIN already exists");
            return;
        }

        Object o = authService.register(request);
        log.info("ADMIN CREATED {}", o);
    }
}