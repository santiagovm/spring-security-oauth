package com.example.logging;

import org.springframework.context.event.EventListener;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class SecurityLogger {

    @EventListener
    public void onAuthenticated(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        System.out.println(String.format(" >>>> login success. Username: [%s]", principal));
    }

    @EventListener
    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        AuthenticationException exception = event.getException();
        System.out.println(String.format(" >>>> authentication failure. Username: [%s] Error: [%s]", principal, exception));
    }

    @EventListener
    public void onAuthorizationFailure(AuthorizationFailureEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        AccessDeniedException exception = event.getAccessDeniedException();
        System.out.println(String.format(" >>>> authorization failure. Username: [%s] Error: [%s]", principal, exception));
    }
}
