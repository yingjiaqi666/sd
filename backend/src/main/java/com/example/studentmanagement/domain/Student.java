package com.example.studentmanagement.domain;

import jakarta.persistence.*;
import com.example.studentmanagement.domain.User;
import lombok.Data;

@Entity
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;
    private String major;

    // 每个学生绑定一个用户账户，用于权限隔离
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
