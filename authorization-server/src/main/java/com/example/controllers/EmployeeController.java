package com.example.controllers;

import com.example.model.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class EmployeeController {

    @PostMapping(value = "/employee", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createEmployee(@RequestBody Employee employee) {
        _fakeEmployeesStorage.add(employee);
    }

    @GetMapping("/employee")
    @ResponseBody
    public Optional<Employee> getEmployee(@RequestParam String email) {
        return _fakeEmployeesStorage
                .stream()
                .filter(employee -> employee.getEmail().equals(email))
                .findAny();
    }

    private List<Employee> _fakeEmployeesStorage = new ArrayList<>();
}
