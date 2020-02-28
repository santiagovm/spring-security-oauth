package com.example.config;

import com.example.web.service.FooService;
import com.example.web.service.IFooService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesFactory {

    @Bean
    public IFooService createService() {
        return new FooService();
    }
}
