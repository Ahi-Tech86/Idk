package com.ahitech.config;

import com.ahitech.enums.AppRole;
import com.ahitech.exception.AppException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final int tokenMaxAge = 3600;

    private final UserAuthenticationProvider provider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.equals("/api/v1/auth/register") || path.equals("/api/v1/auth/login") || path.equals("/api/v1/auth/activate")) {
            filterChain.doFilter(request, response);
            return;
        }

        // extract accessToken from headers or cookies
        String accessToken = extractAccessToken(request);
        // extract refreshToken from cookies
        String refreshToken = extractTokenFromCookies(request, "refreshToken");

        try {
            handleTokens(request, response, accessToken, refreshToken);
            filterChain.doFilter(request, response);
        } catch (AppException exception) {
            SecurityContextHolder.clearContext();
            response.sendError(exception.getHttpStatus().value(), exception.getMessage());
        }
    }

    private void handleTokens(
            HttpServletRequest request,
            HttpServletResponse response,
            String accessToken,
            String refreshToken
    ) {
        if (isTokenInvalid(refreshToken)) {
            log.error("An attempt was made to gain access with an invalid refresh token or missing refresh token");
            throw new AppException("The refresh token was lost or is not valid", HttpStatus.UNAUTHORIZED);
        }

        if (accessToken == null || provider.isAccessTokenExpired(accessToken)) {
            handleRefreshToken(response, refreshToken);
        } else {
            if (!provider.isAccessTokenValid(accessToken)) {
                log.error("An attempt was made to gain access with an invalid access token");
                throw new AppException("The access token is not valid", HttpStatus.UNAUTHORIZED);
            }
            authenticateUser(accessToken);
        }
    }

    private void handleRefreshToken(
            HttpServletResponse response,
            String token
    ) {
        if (provider.isRefreshTokenExpired(token)) {
            log.error("An attempt was made to gain access with an expired refresh token");
            throw new AppException("The refresh token is expired, please authorize again", HttpStatus.UNAUTHORIZED);
        }

        Long userId = provider.extractUserId(token);
        AppRole role = provider.extractRole(token);
        String email = provider.extractEmail(token);

        String newAccessToken = provider.generateAccessToken(userId, email, role);
        log.info("Generated new access token for a user with email {}", email);

        updateTokenCookie(response, newAccessToken);
        authenticateUser(newAccessToken);
    }

    private boolean isTokenInvalid(String token) {
        return token == null || !provider.isRefreshTokenValid(token);
    }

    private void authenticateUser(String token) {
        try {
            SecurityContextHolder.getContext()
                    .setAuthentication(provider.authenticatedAccessValidation(token));
            log.info("Successful authentication occurred");
        } catch (RuntimeException exception) {
            SecurityContextHolder.clearContext();
            log.error("An error occurred during user authentication");
            throw new AppException("Authentication failed", HttpStatus.UNAUTHORIZED);
        }
    }

    private String extractAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null) {
            return authHeader.substring(7);
        }

        return extractTokenFromCookies(request, "accessToken");
    }

    private void updateTokenCookie(
            HttpServletResponse response,
            String token
    ) {
        Cookie tokenCookie = new Cookie("accessToken", token);
        tokenCookie.setPath("/");
        tokenCookie.setHttpOnly(true);
        tokenCookie.setMaxAge(tokenMaxAge);
        response.addCookie(tokenCookie);
    }

    private String extractTokenFromCookies(
            HttpServletRequest request,
            String cookieName
    ) {
        Cookie cookie = WebUtils.getCookie(request, cookieName);

        return (cookie != null) ? cookie.getValue() : null;
    }
}
