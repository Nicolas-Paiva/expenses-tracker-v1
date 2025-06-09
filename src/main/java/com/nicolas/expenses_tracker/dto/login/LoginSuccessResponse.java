package com.nicolas.expenses_tracker.dto.login;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginSuccessResponse extends LoginResponse {

    private String jwtToken;

    public LoginSuccessResponse(String jwtToken) {
        setSuccess(true);
        this.jwtToken = jwtToken;
    }

}
