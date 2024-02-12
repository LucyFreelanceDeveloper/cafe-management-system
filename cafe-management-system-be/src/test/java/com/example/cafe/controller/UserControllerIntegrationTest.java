package com.example.cafe.controller;

import com.example.cafe.config.security.JwtFilter;
import com.example.cafe.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    JwtFilter jwtFilter;

    @Test
    public void testBasicGet() throws Exception {
    }
}
