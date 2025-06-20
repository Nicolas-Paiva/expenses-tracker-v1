package com.nicolas.expenses_tracker.exception;

import com.nicolas.expenses_tracker.dto.GenericErrorResponse;
import com.nicolas.expenses_tracker.dto.RegistrationResponse;
import com.nicolas.expenses_tracker.dto.login.LoginErrorResponse;
import com.nicolas.expenses_tracker.dto.login.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Returns an error response when
     * validation at the controller fails
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        GenericErrorResponse response = GenericErrorResponse.builder()
                .message("Validation failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }


    /**
     * Handles the exception when the user provides an already existing username,
     * or when the username is smaller than 3 characters.
     */
    @ExceptionHandler(InvalidUserNameException.class)
    public ResponseEntity<RegistrationResponse> handleInvalidUsername(InvalidUserNameException e) {
        return ResponseEntity.badRequest().body(RegistrationResponse.failed());
    }


    /**
     * Handles the case where the user tries to login
     * but there is no user or the credentials are incorrect
     */
    @ExceptionHandler(InvalidUserNameOrPassword.class)
    public ResponseEntity<LoginResponse> handleInvalidUsernameOrPassword(InvalidUserNameOrPassword e) {
        return ResponseEntity.badRequest().body(new LoginErrorResponse());
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<?> handleExpenseNotFound() {
        GenericErrorResponse response = new GenericErrorResponse("Expense not found", 404, LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
