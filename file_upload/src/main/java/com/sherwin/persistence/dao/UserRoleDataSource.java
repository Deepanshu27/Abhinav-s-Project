package com.sherwin.persistence.dao;

import com.sherwin.config.security.ApplicationUser;
import com.sherwin.features.uploadingfiles.dto.User;
import com.sherwin.features.uploadingfiles.exception.UserNotFoundException;
import com.sherwin.persistence.entities.UserRoleEntity;
import com.sherwin.persistence.repositories.UserRoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class UserRoleDataSource implements UserDetailsService {

    private final UserRoleRepository userRoleRepository;

    /**
     * Get user and it's all associated roles.
     *
     * @param userId User id of the user.
     * @return User DTO containing user details.
     * @throws UserNotFoundException if user not found.
     */
    private User getUserRoles(@NonNull final String userId) throws UserNotFoundException {
        if (isUserPresent(userId)) {
            final Iterable<UserRoleEntity> userRoleEntities = userRoleRepository.findAllByUserId(userId);
            final User user = new User();
            final List<String> userRoles = new ArrayList<>();
            for (UserRoleEntity userRoleEntity : userRoleEntities) {
                user.setUserName(userRoleEntity.getUserRoleId().getUserName());
                user.setUserId(userRoleEntity.getUserRoleId().getUserId());
                userRoles.add(userRoleEntity.getUserRoleId().getUserRole());
            }
            user.setUserRoles(userRoles);
            return user;
        } else {
            final String message = String.format("User with '%s' id not found.", userId);
            throw new UserNotFoundException(message);
        }
    }

    /**
     * Check if user is present or not.
     *
     * @param userId user id of the user, needs to check.
     * @return true if user found else false.
     */
    private boolean isUserPresent(@NonNull final String userId) {
        return userRoleRepository.existsById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            final User user = getUserRoles(username);
            return new ApplicationUser(user);
        } catch (UserNotFoundException exception) {
            final String message = String.format("User with id '%s' not found.", username);
            throw new UsernameNotFoundException(message, exception);
        }
    }
}
