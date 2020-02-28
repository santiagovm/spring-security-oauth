package com.example.test;

import com.example.web.controller.FooController;
import com.example.web.dto.Foo;
import com.example.web.service.IFooService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FooControllerTest {

    private MockMvc _mockMvc;
    private IFooService _fooService;

    @BeforeEach
    public void beforeEach() {
        _fooService = mock(IFooService.class);
        _mockMvc = MockMvcBuilders.standaloneSetup(new FooController(_fooService)).build();
    }

    @Test
    public void should_return_foo() throws Exception {

        Foo foo = new Foo(333, "foo-333-value");

        when(_fooService.findById(333)).thenReturn(foo);

        _mockMvc.perform(
                get("/foos/333")
        )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value("333"))
                .andExpect(jsonPath("$.name").value("foo-333-value"));
    }

    @Test
    public void should_return_a_different_foo() throws Exception {

        Foo foo = new Foo(444, "foo-444-value");

        when(_fooService.findById(444)).thenReturn(foo);

        _mockMvc.perform(
                get("/foos/444")
        )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value("444"))
                .andExpect(jsonPath("$.name").value("foo-444-value"));
    }
}
