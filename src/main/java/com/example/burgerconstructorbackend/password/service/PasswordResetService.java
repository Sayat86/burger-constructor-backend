package com.example.burgerconstructorbackend.password.service;

import com.example.burgerconstructorbackend.password.dto.PasswordResetConfirmRequest;
import com.example.burgerconstructorbackend.password.dto.PasswordResetRequest;
import com.example.burgerconstructorbackend.common.dto.SuccessResponse;

public interface PasswordResetService {
    SuccessResponse requestReset(PasswordResetRequest request);
    SuccessResponse reset(PasswordResetConfirmRequest request);
}
