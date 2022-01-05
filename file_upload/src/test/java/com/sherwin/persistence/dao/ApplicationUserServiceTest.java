package com.sherwin.persistence.dao;

import com.sherwin.features.uploadingfiles.exception.UserNotFoundException;
import com.sherwin.persistence.entities.UserEntity;
import com.sherwin.persistence.entities.UserRoleId;
import com.sherwin.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class ApplicationUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private ApplicationUserService applicationUserService;

    @BeforeEach
    void setup() {
        openMocks(this);
        applicationUserService = spy(new ApplicationUserService(userRepository, passwordEncoder));
    }

    @Test
    void shouldGetResult() throws UserNotFoundException {
        final UserRoleId userRoleId = new UserRoleId();
        final String testUserId = "1";
        userRoleId.setUserId(testUserId);
        userRoleId.setUserName("Deepanshu");
        userRoleId.setUserRole("admin");
        final UserEntity userEntity = new UserEntity();
        userEntity.setUserRoleId(userRoleId);
        final List<UserEntity> userRoleEntities = Collections.singletonList(userEntity);
        when(userRepository.findAllByUserId(anyString())).thenReturn(userRoleEntities);
        when(applicationUserService.isUserPresent(testUserId)).thenReturn(true);
        final UserDetails userDetails = applicationUserService.getUserRoles(testUserId);
        assertThat(userDetails, is(notNullValue()));
    }

}