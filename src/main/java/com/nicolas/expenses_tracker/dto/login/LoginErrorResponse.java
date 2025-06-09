package com.nicolas.expenses_tracker.dto.login;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginErrorResponse extends LoginResponse {

    private String message;

    public LoginErrorResponse() {
        setSuccess(false);
        message = "Invalid username or password";
    }

}
