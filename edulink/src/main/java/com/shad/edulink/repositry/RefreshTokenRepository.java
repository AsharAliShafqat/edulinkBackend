package com.shad.edulink.repositry;

import com.shad.edulink.dto.RefreshTokenRequest;
import com.shad.edulink.entity.RefreshTokenEntity;
import com.shad.edulink.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity,Long> {
    Optional<RefreshTokenEntity> findByToken(String token);
    Optional<RefreshTokenEntity> findByUser(UserEntity user);
}
