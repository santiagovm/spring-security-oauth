package com.example.test;

import com.example.config.AuthorizationServerApplication;
import com.example.controllers.TokenController;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = { AuthorizationServerApplication.class, TokenController.class })
@AutoConfigureMockMvc
public class TokenControllerTests {

    @Autowired
    private TokenController _tokenController;

    @Autowired
    private MockMvc _mockMvc;

    @SpyBean
    private TokenStore _tokenStore;

    @Test
    public void should_load_context() {
        assertThat(_tokenController).isNotNull();
    }

    @Test
    public void should_return_empty_list_of_tokens_when_token_store_return_null() throws Exception {

        when(_tokenStore.findTokensByClientId("sampleClientId")).thenReturn(null);

        List<String> tokens = getTokens();

        assertThat(tokens).isEmpty();
    }

    @Test
    public void should_return_not_empty_list_of_tokens() throws Exception {

        when(_tokenStore.findTokensByClientId("sampleClientId")).thenReturn(generateTokens(1));

        List<String> tokens = getTokens();

        assertThat(tokens).hasSize(1);
    }

    @Test
    public void should_return_list_of_tokens() throws Exception {

        when(_tokenStore.findTokensByClientId("sampleClientId")).thenReturn(generateTokens(10));

        List<String> tokens = getTokens();

        assertThat(tokens).hasSize(10);
    }

    private List<String> getTokens() throws Exception {

        String tokensString = _mockMvc
                .perform(get("/tokens/sampleClientId"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return new ObjectMapper().readValue(tokensString, new TypeReference<List<String>>(){});
    }

    private List<OAuth2AccessToken> generateTokens(int count) {
        List<OAuth2AccessToken> result = new ArrayList<>();
        IntStream.range(0, count).forEach(n -> result.add(new DefaultOAuth2AccessToken("token" + n)));
        return result;
    }
}

// todo: add TokenRevocationLiveList tests when resource server is complete
