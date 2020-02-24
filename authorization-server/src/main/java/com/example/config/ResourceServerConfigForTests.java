package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

// this is not the standard Resource Server (that is in a different module: resource-server)
// this is for testing OAuth2 with Spring MVC
// notice that is only active via the mvc-test profile
// see OAuthMvcTests
// by the way, this is what changed the response status code from 302 to 401

@EnableResourceServer
@Configuration
@Profile("mvc-test")
public class ResourceServerConfigForTests extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/employee").hasRole("ADMIN");
    }
}
