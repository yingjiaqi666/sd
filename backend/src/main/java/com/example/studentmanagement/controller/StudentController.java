package com.example.studentmanagement.controller;

import com.example.studentmanagement.domain.Student;
import com.example.studentmanagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    // 管理员查看所有；学生仅查看自己的信息（返回单元素列表以适配现有前端）
    @GetMapping
    public List<Student> getAllStudents(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        if (isAdmin) {
            return studentService.getAllStudents();
        }
        return studentService.getStudentByUsername(authentication.getName())
                .map(List::of)
                .orElseGet(List::of);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    // 仅管理员可创建学生（学生注册走 /api/auth/register）
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    // 管理员可编辑任意学生；学生仅能编辑与自己绑定的学生信息
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        if (isAdmin) {
            Student updated = studentService.updateStudentFields(id, student);
            if (updated == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
            return updated;
        }
        // 学生身份，仅允许编辑自己的记录
        Student existing = studentService.getStudentById(id);
        if (existing == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        if (existing.getUser() == null || existing.getUser().getUsername() == null
                || !authentication.getName().equals(existing.getUser().getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to edit this student");
        }
        Student updated = studentService.updateStudentFields(id, student);
        if (updated == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        return updated;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        // 管理员删除学生时同时删除其对应的用户（将级联清理学生记录）
        studentService.deleteStudentAndUser(id);
    }
}
