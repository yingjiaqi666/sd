package com.example.studentmanagement.controller;

import com.example.studentmanagement.domain.Student;
import com.example.studentmanagement.domain.User;
import com.example.studentmanagement.dto.LoginRequest;
import com.example.studentmanagement.dto.RegisterRequest;
import com.example.studentmanagement.dto.UserInfoResponse;
import com.example.studentmanagement.dto.LoginInfo;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.UserRepository;
import com.example.studentmanagement.service.LoginCacheService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginCacheService loginCacheService;

    public AuthController(UserRepository userRepository,
                          StudentRepository studentRepository,
                          PasswordEncoder passwordEncoder,
                          LoginCacheService loginCacheService) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginCacheService = loginCacheService;
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest) {
        // Spring Security handles authentication via formLogin/session
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> me(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName());
        UserInfoResponse resp = new UserInfoResponse();
        resp.setUsername(user.getUsername());
        resp.setRole(user.getRole());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/cache/me")
    public ResponseEntity<LoginInfo> cacheMe(Authentication authentication) {
        LoginInfo info = loginCacheService.get(authentication.getName());
        if (info == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(info);
    }

    // 仅允许注册为 STUDENT 角色
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("STUDENT");
        User saved = userRepository.save(user);

        Student student = new Student();
        student.setName(request.getName());
        student.setAge(request.getAge());
        student.setMajor(request.getMajor());
        student.setUser(saved);
        studentRepository.save(student);

        return ResponseEntity.ok("Registered");
    }
}
