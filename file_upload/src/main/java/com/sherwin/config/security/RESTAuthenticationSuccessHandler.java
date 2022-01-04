package com.sherwin.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sherwin.features.uploadingfiles.dto.User;
import com.sherwin.features.uploadingfiles.exception.UserNotFoundException;
import com.sherwin.persistence.dao.UserRoleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RESTAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    UserRoleDataSource userDao;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void onAuthenticationSuccess(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final Authentication authentication
    ) throws IOException, ServletException {
        clearAuthenticationAttributes(request);
        final String userName = authentication.getName();
        try {
            final UserDetails user = userDao.loadUserByUsername(userName);
            response.getWriter().write(objectMapper.writeValueAsString(user));
            response.setStatus(HttpStatus.OK.value());
        } catch (UsernameNotFoundException userNotFoundException) {
            response.getWriter().write(userNotFoundException.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            throw new ServletException("User not found.");
        }
    }
}
