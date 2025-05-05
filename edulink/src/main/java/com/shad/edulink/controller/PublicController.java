package com.shad.edulink.controller;

import com.shad.edulink.dto.*;
import com.shad.edulink.entity.RefreshTokenEntity;
import com.shad.edulink.service.JwtService;
import com.shad.edulink.service.PublicService;
import com.shad.edulink.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final PublicService publicService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


    @Autowired
    public PublicController(PublicService userService, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.publicService = userService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDetailsResponse> createUser(
            @Validated @RequestBody UserRegisterRequest userRequestDTO) {
        try {
            UserDetailsResponse userResponseDTO = publicService.userRegister(userRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUserRequest(@RequestBody UserLogInRequest userLogInRequest){
        try{
            JwtResponse response = publicService.loginUser(userLogInRequest);
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshTokenEntity::getUser)
                .map(userEntity -> {
                    String token = jwtService.generateToken(userEntity.getUsername());
                    JwtResponse jwtResponse = new JwtResponse();
                    jwtResponse.setAccessToken(token);
                    jwtResponse.setToken(refreshTokenRequest.getToken());
                    return ResponseEntity.ok(jwtResponse);
                })
                .orElseThrow(()-> new RuntimeException("Refresh TokenNot Found"));
    }
}
