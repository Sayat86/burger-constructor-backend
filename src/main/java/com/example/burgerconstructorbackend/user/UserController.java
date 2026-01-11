package com.example.burgerconstructorbackend.user;

import com.example.burgerconstructorbackend.user.dto.UpdateUserRequest;
import com.example.burgerconstructorbackend.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    @PatchMapping("/user")
    public Map<String, Object> updateUser(
            @RequestBody UpdateUserRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        if (request.email() != null) {
            user.setEmail(request.email());
        }
        if (request.name() != null) {
            user.setName(request.name());
        }
        if (request.password() != null) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        userRepository.save(user);

        return Map.of(
                "success", true,
                "user", new UserDto(user.getEmail(), user.getName())
        );
    }

}
