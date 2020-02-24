package com.example.web.dto;

public class Foo {

    public long getId() { return _id; }
    public void setId(final long id) { _id = id; }

    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }

    public Foo() {}

    public Foo(final long id, final String name) {
        _id = id;
        _name = name;
    }

    private long _id;
    private String _name;
}
