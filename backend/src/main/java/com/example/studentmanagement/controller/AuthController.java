package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.LoginRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest) {
        // Spring Security handles authentication
    }
}
