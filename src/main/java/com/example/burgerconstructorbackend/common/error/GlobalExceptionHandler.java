package com.example.burgerconstructorbackend.common.error;

import com.example.burgerconstructorbackend.auth.exception.BadRequestException;
import com.example.burgerconstructorbackend.auth.exception.NotFoundException;
import com.example.burgerconstructorbackend.auth.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorized(UnauthorizedException ex) {
        return new ErrorResponse(false, ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(BadRequestException ex) {
        return new ErrorResponse(false, ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException ex) {
        return new ErrorResponse(false, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOther(Exception ex) {
        return new ErrorResponse(false, "Internal server error");
    }
}


