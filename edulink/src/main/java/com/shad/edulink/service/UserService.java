package com.shad.edulink.service;

import com.shad.edulink.dto.*;
import com.shad.edulink.entity.RefreshTokenEntity;
import com.shad.edulink.entity.StudentEntity;
import com.shad.edulink.entity.TeacherEntity;
import com.shad.edulink.mapper.Mappers;
import com.shad.edulink.repositry.StudentRepository;
import com.shad.edulink.repositry.TeacherRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public UserService(TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public void assignStudentToTeacher(String studentName, String teacherName) {
        StudentEntity student = studentRepository.findByUsername(studentName)
                .orElseThrow(() -> new RuntimeException("Student Not Found"));
        TeacherEntity teacher = teacherRepository.findByUsername(teacherName)
                .orElseThrow(() -> new RuntimeException("Teacher Not Found"));
        if (!teacher.getStudents().contains(student)) {
            teacher.getStudents().add(student);
            teacherRepository.save(teacher);
        }else{
            throw new IllegalStateException("Student is already assigned to this teacher");
        }
    }

}