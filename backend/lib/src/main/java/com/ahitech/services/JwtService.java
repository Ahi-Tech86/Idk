package com.ahitech.services;

import com.ahitech.enums.AppRole;
import org.springframework.security.core.Authentication;

import java.util.Date;

public interface JwtService {
    Long extractUserId(String token);
    AppRole extractRole(String token);
    String extractEmail(String token);
    boolean isAccessTokenValid(String token);
    boolean isRefreshTokenValid(String token);
    boolean isAccessTokenExpired(String token);
    boolean isRefreshTokenExpired(String token);
    Date extractRefreshTokenExpirationTime(String token);
    Authentication authenticatedAccessValidation(String token);
    String generateAccessToken(Long id, String email, AppRole role);
    String generateRefreshToken(Long id, String email, AppRole role);
}
