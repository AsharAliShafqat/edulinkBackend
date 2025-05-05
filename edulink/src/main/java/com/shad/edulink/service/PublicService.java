package com.shad.edulink.service;

import com.shad.edulink.dto.JwtResponse;
import com.shad.edulink.dto.UserDetailsResponse;
import com.shad.edulink.dto.UserLogInRequest;
import com.shad.edulink.dto.UserRegisterRequest;
import com.shad.edulink.entity.RefreshTokenEntity;
import com.shad.edulink.entity.StudentEntity;
import com.shad.edulink.entity.TeacherEntity;
import com.shad.edulink.mapper.Mappers;
import com.shad.edulink.repositry.StudentRepository;
import com.shad.edulink.repositry.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PublicService {
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final Mappers userMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public PublicService(TeacherRepository teacherRepository, StudentRepository studentRepository, Mappers userMapper, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public UserDetailsResponse userRegister(UserRegisterRequest userRequestDTO) {
        String role = userRequestDTO.getRole().toString().toUpperCase();

        if (role.equals("STUDENT")) {
            StudentEntity student = userMapper.userRequestDtoToStudentEntity(userRequestDTO);
            student.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
            StudentEntity savedStudent = studentRepository.save(student);
            return userMapper.userEntityToUserResponseDto(savedStudent);

        } else if (role.equals("TEACHER")) {
            TeacherEntity teacher = userMapper.userRequestDtoToTeacherEntity(userRequestDTO);
            teacher.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
            TeacherEntity savedTeacher = teacherRepository.save(teacher);
            return userMapper.userEntityToUserResponseDto(savedTeacher);
        }

        throw new IllegalArgumentException("Invalid user role: " + role);
    }

    public JwtResponse loginUser(UserLogInRequest userLogInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLogInRequest.getUsername(), userLogInRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(userLogInRequest.getUsername());
            JwtResponse jwtResponse = JwtResponse.builder()
                    .accessToken(jwtService.generateToken(userLogInRequest.getUsername()))
                    .token(refreshToken.getToken())
                    .build();

            return jwtResponse;
        } else {
            throw new IllegalStateException("Bad credentials");
        }
    }
}
