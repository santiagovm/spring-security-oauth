package com.example.test;

import com.example.config.AuthorizationServerApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@SpringBootTest(classes = AuthorizationServerApplication.class)
@ActiveProfiles("mvc-test")
public class OAuthMvcTests {

    private final String USER_EMAIL = "jim@yahoo.com";
    private final String USER_FIRST_NAME = "Jim";
    private final String CLIENT_ID = "fooClientIdPassword";
    private final String CLIENT_SECRET = "secret";
    private final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    private MockMvc _mockMvc;

    @Autowired
    private WebApplicationContext _webApplicationContext;

    @Autowired
    private FilterChainProxy _springSecurityFilterChain;

    @BeforeEach
    public void beforeEach() {
        _mockMvc = MockMvcBuilders
                .webAppContextSetup(_webApplicationContext)
                .addFilter(_springSecurityFilterChain)
                .build();
    }

    @Test
    public void given_no_token_when_get_secure_request_then_unauthorized() throws Exception {

        _mockMvc.perform(get("/employee").param("email", USER_EMAIL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void given_invalid_role_when_get_secure_request_then_forbidden() throws Exception {

        final String accessToken = getAccessToken("user1", "pass");
        System.out.println(" >>> accessToken = " + accessToken);

        _mockMvc.perform(
                get("/employee")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("email", USER_EMAIL)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void given_token_when_post_get_secure_request_then_ok() throws Exception {

        final String accessToken = getAccessToken("admin", "nimda");

        String employeeString = "{\"email\":\"" + USER_EMAIL + "\",\"name\":\"" + USER_FIRST_NAME + "\",\"age\":30}";

        _mockMvc.perform(
                post("/employee")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(CONTENT_TYPE_JSON)
                        .content(employeeString)
                        .accept(CONTENT_TYPE_JSON)
        ).andExpect(
                status().isCreated()
        );

        _mockMvc.perform(
                get("/employee")
                        .param("email", USER_EMAIL)
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(CONTENT_TYPE_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON))
                .andExpect(jsonPath("$.name", is(USER_FIRST_NAME)));
    }

    private String getAccessToken(String username, String password) throws Exception {

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("grant_type", "password");
        params.add("client_id", CLIENT_ID);
        params.add("username", username);
        params.add("password", password);

        ResultActions result = _mockMvc.perform(
                post("/oauth/token")
                        .params(params)
                        .with(httpBasic(CLIENT_ID, CLIENT_SECRET))
                        .accept(CONTENT_TYPE_JSON)
            )
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON));

        String resultString = result.andReturn().getResponse().getContentAsString();

        return new JacksonJsonParser()
                .parseMap(resultString)
                .get("access_token")
                .toString();
    }
}
