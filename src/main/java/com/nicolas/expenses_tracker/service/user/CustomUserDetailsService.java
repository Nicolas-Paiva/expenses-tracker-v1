package com.nicolas.expenses_tracker.service.user;

import com.nicolas.expenses_tracker.model.user.User;
import com.nicolas.expenses_tracker.model.user.UserPrincipal;
import com.nicolas.expenses_tracker.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Defines how the user is retrieved from
 * the datasource, which in this case,
 * comes from PostgreSQL
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserPrincipal(user);
    }

}
