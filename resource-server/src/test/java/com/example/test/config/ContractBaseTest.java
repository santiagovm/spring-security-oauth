package com.example.test.config;

import com.example.config.ResourceServerApplication;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles(profiles = "contract-testing")
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ResourceServerApplication.class,
        FakeTokenServicesFactory.class,
        FakeServicesFactory.class
})
public abstract class ContractBaseTest {

    @Autowired
    private WebApplicationContext _webApplicationContext;

    @Autowired
    private FilterChainProxy _springSecurityFilterChain;

    @Before
    public void beforeEach() {

        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(_webApplicationContext)
                .addFilter(_springSecurityFilterChain)
                .build();

        RestAssuredMockMvc.mockMvc(mockMvc);
    }
}
