package com.example.web.service;

import com.example.web.dto.Foo;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

public class FooService implements IFooService {

    public Foo findById(long id) {
        return new Foo(
                Long.parseLong(randomNumeric(2)),
                randomAlphabetic(4)
        );
    }
}
