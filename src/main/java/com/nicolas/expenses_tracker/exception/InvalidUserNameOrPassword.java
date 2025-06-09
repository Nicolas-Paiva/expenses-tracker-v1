package com.nicolas.expenses_tracker.exception;

import lombok.Data;

@Data
public class InvalidUserNameOrPassword extends RuntimeException{

    private String message;

    public InvalidUserNameOrPassword() {
        message = "Invalid username or password";
    }

}
