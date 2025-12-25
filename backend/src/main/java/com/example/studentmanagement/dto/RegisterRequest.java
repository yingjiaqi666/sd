package com.example.studentmanagement.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String name;
    private int age;
    private String major;
}