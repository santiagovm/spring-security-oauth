package com.example.test.config;

import com.example.test.stubs.FooServiceStub;
import com.example.web.service.IFooService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("contract-testing")
public class FakeServicesFactory {

    @Bean
    @Primary
    public IFooService createStubbedService() {
        return new FooServiceStub();
    }
}
