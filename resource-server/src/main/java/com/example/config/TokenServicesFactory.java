package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
public class TokenServicesFactory {

    @Bean
    public ResourceServerTokenServices createTokenServices() {

        RemoteTokenServices tokenServices = new RemoteTokenServices();

        // todo: bring this from app properties
        tokenServices.setCheckTokenEndpointUrl("http://localhost:8081/spring-security-oauth-server/oauth/check_token");
        tokenServices.setClientId("fooClientIdPassword");
        tokenServices.setClientSecret("secret");

        return tokenServices;
    }
}
