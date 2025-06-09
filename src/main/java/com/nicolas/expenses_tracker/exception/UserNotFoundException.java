package com.nicolas.expenses_tracker.exception;

import lombok.Data;

@Data
public class UserNotFoundException extends RuntimeException {

    private String message;

    public UserNotFoundException() {
        message = "User not found";
    }

}
