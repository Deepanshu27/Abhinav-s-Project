package com.sherwin.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sherwin.persistence.dao.ApplicationUserService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class RESTAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ApplicationUserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        clearAuthenticationAttributes(request);
        String userName = authentication.getName();
        try {
            UserDetails user = userService.loadUserByUsername(userName);
            response.getWriter().write(objectMapper.writeValueAsString(user));
            response.setStatus(200);
        } catch (UsernameNotFoundException userNotFoundException) {
            logger.error(String.format("User with '%S' user name not found", userName));
            response.getWriter().write(objectMapper.writeValueAsString(String.format("User with '%S' user name not found", userName)));
            response.setStatus(500);
            throw new ServletException("User not found.");
        }
    }
}
