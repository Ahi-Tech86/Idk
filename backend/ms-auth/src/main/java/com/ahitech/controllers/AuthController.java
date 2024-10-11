package com.ahitech.controllers;

import com.ahitech.dtos.UserDto;
import com.ahitech.dtos.SignInRequest;
import com.ahitech.dtos.SignUpRequest;
import com.ahitech.dtos.ActivateUserRequest;
import com.ahitech.services.AuthServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authService.register(signUpRequest));
    }

    @PostMapping("/activate")
    public ResponseEntity<UserDto> activateUser(@RequestBody ActivateUserRequest activateUserRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.activate(activateUserRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> authenticateUser(HttpServletResponse response, @RequestBody SignInRequest signInRequest) {
        List<Object> authenticatedUser = authService.login(signInRequest);

        UserDto userDto = (UserDto) authenticatedUser.get(0);
        String accessToken = (String) authenticatedUser.get(1);
        String refreshToken = (String) authenticatedUser.get(2);

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(60 * 60);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        deleteCookie("accessToken", response);
        deleteCookie("refreshToken", response);

        return ResponseEntity.noContent().build();
    }

    private void deleteCookie(String cookieName, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    //todo: limits trying to log in, api docs
}
