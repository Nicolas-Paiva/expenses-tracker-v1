package com.nicolas.expenses_tracker.dto.user;

import com.nicolas.expenses_tracker.model.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the DTO the client must
 * provide in order to perform user
 * registration
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, message = "Username must have at least 3 characters")
    private String userName;

    @NotBlank
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String password;


    public static User toUser(UserDTO userDto) {
        return User.builder()
                .username(userDto.getUserName())
                .password(userDto.getPassword())
                .build();
    }


    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .userName(user.getUsername())
                .password(user.getPassword())
                .build();
    }

}
