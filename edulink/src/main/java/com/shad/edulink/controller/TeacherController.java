package com.shad.edulink.controller;

import com.shad.edulink.dto.TeacherAssignmentDTO;
import com.shad.edulink.dto.UserDetailsResponse;
import com.shad.edulink.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private UserService userService;

    @Autowired
    public TeacherController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/student-to-teacher")
    public ResponseEntity<String> assignStudentToTeacher(@RequestBody TeacherAssignmentDTO teacherAssignmentDTO){
        try{
            userService.assignStudentToTeacher(teacherAssignmentDTO.getStudentUsername(),teacherAssignmentDTO.getTeacherUsername());
            return new ResponseEntity<>("Student Assigned To Teacher", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

}
