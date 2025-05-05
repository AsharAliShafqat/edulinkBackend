package com.shad.edulink.repositry;

import com.shad.edulink.entity.StudentEntity;
import com.shad.edulink.entity.TeacherEntity;
import com.shad.edulink.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentEntity,Long> {
    Optional<StudentEntity> findByUsername(String username);
}
