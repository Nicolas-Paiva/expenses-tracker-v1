package com.nicolas.expenses_tracker.dto;

import com.nicolas.expenses_tracker.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationResponse {

    private boolean created;

    private String message;


    /**
     * Returns the success message when the user is
     * created successfully
     */
    public static RegistrationResponse success(UserDTO userDTO) {
        return RegistrationResponse.builder()
                .created(true)
                .message("User " + userDTO.getUserName() + " created successfully!")
                .build();
    }

    public static RegistrationResponse failed() {
        return RegistrationResponse.builder()
                .created(false)
                .message("Username already exists")
                .build();
    }

}
