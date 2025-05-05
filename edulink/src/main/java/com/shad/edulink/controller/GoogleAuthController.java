package com.shad.edulink.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class GoogleAuthController {

    @GetMapping("/")
    public String home() {
        return "Welcome to EduLink!";
    }

    @GetMapping("/user")
    public Map<String, Object> getUser(OAuth2AuthenticationToken authentication) {
        if (authentication == null) {
            throw new RuntimeException("No user authenticated");
        }
        OAuth2User oAuth2User = authentication.getPrincipal();
        return oAuth2User.getAttributes();
    }
}
