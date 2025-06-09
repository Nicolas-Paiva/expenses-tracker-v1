package com.nicolas.expenses_tracker.service.user;

import com.nicolas.expenses_tracker.dto.RegistrationResponse;
import com.nicolas.expenses_tracker.dto.user.UserDTO;
import com.nicolas.expenses_tracker.dto.login.LoginSuccessResponse;
import com.nicolas.expenses_tracker.exception.InvalidUserNameException;
import com.nicolas.expenses_tracker.exception.InvalidUserNameOrPassword;
import com.nicolas.expenses_tracker.model.user.User;
import com.nicolas.expenses_tracker.repository.user.UserRepository;
import com.nicolas.expenses_tracker.security.jwt.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service related to user registration
 * and login
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    private final JWTService jwtService;


    /**
     * Retrieves a user based on the username
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public RegistrationResponse register(UserDTO userDTO) {

        // Checks whether the username already exists
        if (userRepository.findByUsername(userDTO.getUserName()).isPresent()) {
            throw InvalidUserNameException.userNameAlreadyExists();
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(UserDTO.toUser(userDTO));

        return RegistrationResponse.success(userDTO);
    }


    /**
     * Logs the user in using the AuthenticationManager
     * and issuing a JWT to the user.
     *
     * If the user fails to authenticate, an InvalidUserNameOrPassword
     * exception is thrown, which causes a LoginErrorResponse to be sent
     * to the user.
     */
    public LoginSuccessResponse login(UserDTO userDTO) {
        Authentication authentication;

        User user = UserDTO.toUser(userDTO);

        // Tries to authenticate the user
        try {
             authentication = authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken
                                    (user.getUsername(), user.getPassword()));
        } catch(Exception e) {
            throw new InvalidUserNameOrPassword();
        }

        if (authentication.isAuthenticated()) {
            String jwt = jwtService.generateToken(user.getUsername());
            return new LoginSuccessResponse(jwt);
        }

        throw new InvalidUserNameOrPassword();
    }

}
