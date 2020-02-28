package com.example.test.stubs;

import com.example.web.dto.Foo;
import com.example.web.service.IFooService;

public class FooServiceStub implements IFooService {

    @Override
    public Foo findById(long id) {
        return new Foo(333, "foo-value-333");
    }
}
