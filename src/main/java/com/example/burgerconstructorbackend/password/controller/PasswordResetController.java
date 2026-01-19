package com.example.burgerconstructorbackend.password.controller;

import com.example.burgerconstructorbackend.password.dto.PasswordResetConfirmRequest;
import com.example.burgerconstructorbackend.password.dto.PasswordResetRequest;
import com.example.burgerconstructorbackend.common.dto.SuccessResponse;
import com.example.burgerconstructorbackend.password.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/password-reset")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping
    public SuccessResponse requestReset(@RequestBody PasswordResetRequest request) {
        return passwordResetService.requestReset(request);
    }

    @PostMapping("/reset")
    public SuccessResponse reset(@RequestBody PasswordResetConfirmRequest request) {
        return passwordResetService.reset(request);
    }
}

