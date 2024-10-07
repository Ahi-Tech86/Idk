package com.ahitech.controllers;

import com.ahitech.dtos.UserDto;
import com.ahitech.dtos.SignInRequest;
import com.ahitech.dtos.SignUpRequest;
import com.ahitech.dtos.ActivateUserRequest;
import com.ahitech.services.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/register")
    public String registerUser(@RequestBody SignUpRequest signUpRequest) {
        return null;
    }

    @PostMapping("/activate")
    public UserDto activateUser(@RequestBody ActivateUserRequest activateUserRequest) {
        return null;
    }

    @PostMapping("/login")
    public UserDto authenticateUser(@RequestBody SignInRequest signInRequest) {
        return null;
    }
}
