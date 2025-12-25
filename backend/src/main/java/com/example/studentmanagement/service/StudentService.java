package com.example.studentmanagement.service;

import com.example.studentmanagement.domain.Student;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // 管理员删除学生时，连同其对应的用户一并删除
    // 先删学生再删用户，避免外键约束阻止删除用户
    @Transactional
    public void deleteStudentAndUser(Long id) {
        Optional<Student> opt = studentRepository.findById(id);
        if (opt.isPresent()) {
            Student s = opt.get();
            Long userId = (s.getUser() != null) ? s.getUser().getId() : null;
            // 先删除学生记录
            studentRepository.deleteById(id);
            // 再删除其关联用户（如存在）
            if (userId != null) {
                userRepository.deleteById(userId);
            }
            return;
        }
        // 不存在则不操作（或抛异常，按当前控制器逻辑返回404）
    }

    // 仅更新允许的字段，保持用户关联不变
    public Student updateStudentFields(Long id, Student incoming) {
        Optional<Student> opt = studentRepository.findById(id);
        if (opt.isEmpty()) {
            return null;
        }
        Student existing = opt.get();
        existing.setName(incoming.getName());
        existing.setAge(incoming.getAge());
        existing.setMajor(incoming.getMajor());
        return studentRepository.save(existing);
    }

    public Optional<Student> getStudentByUsername(String username) {
        return studentRepository.findByUserUsername(username);
    }
}
