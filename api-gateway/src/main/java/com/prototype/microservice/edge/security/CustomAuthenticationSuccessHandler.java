package com.prototype.microservice.edge.security;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.prototype.microservice.edge.event.AdminConsoleLoginEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * Authentication success handler
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    public CustomAuthenticationSuccessHandler() {
    }

    @Autowired
    private ApplicationEventPublisher appEventPublisher;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        appEventPublisher.publishEvent(new AdminConsoleLoginEvent(
                MessageFormat.format("User ({0}) has logged into the admin console successfully from IP address ({1})",
                        new Object[]{
                                authentication.getPrincipal(),
                                request.getRemoteAddr()
                        })));

        redirectStrategy.sendRedirect(request, response, request.getRequestURL().toString());

    }

}
