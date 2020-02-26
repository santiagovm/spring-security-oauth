package com.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecurityLogger {

    @EventListener
    public void onAuthenticated(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        log.debug(String.format("login success. Username: [%s]", principal));
    }

    @EventListener
    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        AuthenticationException exception = event.getException();
        log.debug(String.format("authentication failure. Username: [%s]", principal), exception);
    }

    @EventListener
    public void onAuthorizationFailure(AuthorizationFailureEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        AccessDeniedException exception = event.getAccessDeniedException();
        log.debug(String.format("authorization failure. Username: [%s]", principal), exception);
    }
}
