package com.sherwin.persistence.dao;

import com.google.common.annotations.VisibleForTesting;
import com.sherwin.features.uploadingfiles.exception.UserNotFoundException;
import com.sherwin.persistence.entities.UserEntity;
import com.sherwin.persistence.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ApplicationUserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Get user and it's all associated roles.
     *
     * @param userId User id of the user.
     * @return User DTO containing user details.
     * @throws UserNotFoundException if user not found.
     */
    @NonNull
    @VisibleForTesting
    UserDetails getUserRoles(@NonNull final String userId) throws UserNotFoundException {
        if (isUserPresent(userId)) {
            final Iterable<UserEntity> userRoleEntities = userRepository.findUserRoleById(userId);
            final List<String> userRoles = new ArrayList<>();
            String password = null;
            for(UserEntity userEntity : userRoleEntities) {
                password = userEntity.getPassword();
                userRoles.add(userEntity.getUserRoleId().getUserRole());
            }
            return User.builder()
                    .username(userId)
                    .password(passwordEncoder.encode(password))
                    .roles(userRoles.toArray(new String[0]))
                    .build();
        } else {
            final String message = String.format("User with id '%s' not found.", userId);
            throw new UserNotFoundException(message);
        }
    }

    /**
     * Check if user is present or not.
     *
     * @param userId user id of the user, needs to check.
     * @return true if user found else false.
     */
    @VisibleForTesting
    boolean isUserPresent(@NonNull final String userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return getUserRoles(username);
        } catch (UserNotFoundException exception) {
            final String message = String.format("User with id '%s' not found.", username);
            throw new UsernameNotFoundException(message, exception);
        }
    }
}
