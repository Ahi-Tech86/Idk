package com.ahitech.config;

import com.ahitech.enums.AppRole;
import com.ahitech.services.JwtServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider {

    private final JwtServiceImpl service;

    public Long extractUserId(String token) {
        return service.extractUserId(token);
    }

    public AppRole extractRole(String token) {
        return service.extractRole(token);
    }

    public String extractEmail(String token) {
        return service.extractEmail(token);
    }

    public boolean isAccessTokenValid(String token) {
        return service.isAccessTokenValid(token);
    }

    public boolean isRefreshTokenValid(String token) {
        return service.isRefreshTokenValid(token);
    }

    public boolean isAccessTokenExpired(String token) {
        return service.isAccessTokenExpired(token);
    }

    public boolean isRefreshTokenExpired(String token) {
        return service.isRefreshTokenExpired(token);
    }

    public Authentication authenticatedAccessValidation(String token) {
        return service.authenticatedAccessValidation(token);
    }

    public String generateAccessToken(Long id, String email, AppRole role) {
        return service.generateAccessToken(id, email, role);
    }
}
