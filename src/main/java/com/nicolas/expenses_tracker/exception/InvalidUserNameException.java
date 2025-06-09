package com.nicolas.expenses_tracker.exception;

import lombok.Data;

@Data
public class InvalidUserNameException extends RuntimeException {

    private String message;

    public static InvalidUserNameException userNameAlreadyExists() {
        InvalidUserNameException e = new InvalidUserNameException();
        e.setMessage("Username already exists");
        return e;
    }

    public static InvalidUserNameException invalidUsername() {
        InvalidUserNameException e = new InvalidUserNameException();
        e.setMessage("Username is too short");
        return e;
    }

}
