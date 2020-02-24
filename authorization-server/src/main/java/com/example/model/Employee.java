package com.example.model;

public class Employee {

    public String getEmail() { return _email; }
    public void setEmail(String email) { _email = email; }

    public String getName() { return _name; }
    public void setName(String name) { _name = name; }

    public Employee() {}

    public Employee(String email, String name) {
        _email = email;
        _name = name;
    }

    private String _email;
    private String _name;
}
