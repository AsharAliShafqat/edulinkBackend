package com.shad.edulink.repositry;

import com.shad.edulink.entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<TeacherEntity,Long> {
    Optional<TeacherEntity> findByUsername(String username);
}
