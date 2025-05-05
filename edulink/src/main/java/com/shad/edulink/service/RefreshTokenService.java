package com.shad.edulink.service;

import com.shad.edulink.dto.RefreshTokenRequest;
import com.shad.edulink.entity.RefreshTokenEntity;
import com.shad.edulink.entity.UserEntity;
import com.shad.edulink.repositry.RefreshTokenRepository;
import com.shad.edulink.repositry.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshTokenEntity createRefreshToken(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1. Delete old refresh token if exists (Important)
        tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);

        // 2. Now create new token
        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString()); // fresh UUID every time
        refreshToken.setExpiryDate(Instant.now().plus(Duration.ofMinutes(60)));

        // Log the expiration time before saving
        System.out.println("Refresh Token Expiry Date: " + refreshToken.getExpiryDate().toString());

        // Save the refresh token and log the saved token
        RefreshTokenEntity savedToken = tokenRepository.save(refreshToken);
        System.out.println("Saved Token: " + savedToken);

        return savedToken;
    }

    public Optional<RefreshTokenEntity> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            tokenRepository.delete(token);
            throw new RuntimeException(token.getToken()+" Refresh Token was expired please login again");
        }else {
            return token;
        }
    }


}
