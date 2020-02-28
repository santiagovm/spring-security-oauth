package com.example.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// inspiration from
// https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/java/org/springframework/security/oauth2/provider/authentication/OAuth2AuthenticationManagerTests.java

@Configuration
@Profile("contract-testing")
public class FakeTokenServicesFactory {

    @Bean
    @Primary
    public ResourceServerTokenServices createStubbedTokenServicesThatMarksAllAccessTokensAsValid() {

        OAuth2Authentication oauth2Auth = createOAuth2Authentication();
        DefaultOAuth2AccessToken accessToken = createAccessToken();

        ResourceServerTokenServices tokenServicesMock = mock(ResourceServerTokenServices.class);

        when(tokenServicesMock.loadAuthentication(anyString())).thenReturn(oauth2Auth);
        when(tokenServicesMock.readAccessToken(anyString())).thenReturn(accessToken);

        return tokenServicesMock;
    }

    private DefaultOAuth2AccessToken createAccessToken() {

        DefaultOAuth2AccessToken accessToken = new DefaultOAuth2AccessToken("foo-token");

        // api method being tested requires read scope
        accessToken.setScope(READ_SCOPE);

        // token must expire at some point in the future, otherwise it will be marked as invalid
        final int TEN_MINUTES = 10 * 60 * 1000;
        accessToken.setExpiration(new Date(System.currentTimeMillis() + TEN_MINUTES));

        return accessToken;
    }

    private OAuth2Authentication createOAuth2Authentication() {

        OAuth2Request storedRequest = createOAuth2Request("foo-client-id", true);

        // authorities make the Authentication.isAuthenticated = true
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_USER")
        );

        Authentication userAuth = new UsernamePasswordAuthenticationToken(
                "foo",
                "bar",
                authorities
        );

        OAuth2Authentication oauth2Auth = new OAuth2Authentication(storedRequest, userAuth);
        oauth2Auth.setAuthenticated(true);

        return oauth2Auth;
    }

    private OAuth2Request createOAuth2Request(String clientId, boolean isApproved) {

        Map<String, String> emptyRequestParameters = Collections.<String, String> emptyMap();

        return new OAuth2Request(
                emptyRequestParameters,
                clientId,
                null,
                isApproved,
                READ_SCOPE, // api method being tested requires read scope
                null,
                null,
                null,
                null
        );
    }

    final private HashSet<String> READ_SCOPE = new HashSet<>(Collections.singletonList("read"));
}
