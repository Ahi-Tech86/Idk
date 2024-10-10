package com.ahitech.controllers;

import com.ahitech.enums.AppRole;
import com.ahitech.services.JwtServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestContoller {

    private final JwtServiceImpl service;

    @GetMapping("/testToken")
    public String getToken(@RequestParam String email, @RequestParam Long id) {
        return service.generateAccessToken(id, email, AppRole.USER);
    }

    @GetMapping("/testValid")
    public Boolean getValid(@RequestParam String token) {
        return service.isRefreshTokenValid(token);
    }

    @GetMapping("/refresh")
    public Boolean getRefresh(@RequestParam String email, @RequestParam Long id) {
        String token = service.generateRefreshToken(id, email, AppRole.USER);
        return service.isRefreshTokenValid(token);
    }

    @GetMapping("/aboba")
    public String test() {
        return "Aboba";
    }
}
