package com.example.studentmanagement.dto;

import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class LoginInfo {
    private String username;
    private List<String> roles;
    private Instant loginTime = Instant.now();
}