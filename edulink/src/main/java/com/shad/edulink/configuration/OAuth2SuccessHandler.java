package com.shad.edulink.configuration;

import com.shad.edulink.entity.RefreshTokenEntity;
import com.shad.edulink.entity.UserEntity;
import com.shad.edulink.enums.Role;
import com.shad.edulink.repositry.UserRepository;
import com.shad.edulink.service.JwtService;
import com.shad.edulink.service.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RefreshTokenService createRefreshToken;


    @Autowired
    public OAuth2SuccessHandler(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // Debugging: Log the OAuth2User attributes
        System.out.println("OAuth2User attributes: " + oAuth2User.getAttributes());

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Check if email and name are being retrieved correctly
        if (email == null || name == null) {
            System.out.println("Error: Missing email or name from OAuth2User.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Email or name missing in OAuth2 response.\"}");
            return;
        }

        // Check if the user already exists
        UserEntity user = userRepository.findByEmail(email);

        if (user == null) {
            // Register new user
            user = new UserEntity();
            user.setEmail(email);
            user.setUsername(name);
            user.setRole(Role.valueOf("STUDENT"));
            user.setPassword(passwordEncoder.encode("oauth2user"));
            userRepository.save(user);

            System.out.println("User registered: " + name);
        }

        // Generate JWT Token
        String accessToken = jwtService.generateToken(email);

        // Generate refresh token
        RefreshTokenEntity refreshTokenEntity = createRefreshToken.createRefreshToken(user.getUsername());
        String refreshToken = refreshTokenEntity.getToken();

        // Build JSON response
        String jsonResponse = String.format(
                "{\"accessToken\": \"%s\", \"refreshToken\": \"%s\"}", accessToken, refreshToken
        );

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }

}
