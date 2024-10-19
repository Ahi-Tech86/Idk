package com.ahitech.controllers;

import com.ahitech.services.JwtServiceImpl;
import com.ahitech.services.SubscriptionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class SubsController {

    private final JwtServiceImpl jwtService;
    private final SubscriptionServiceImpl subsService;

    @PostMapping("/{userId}/sub")
    public ResponseEntity<String> subscribeOnAccount(
            @PathVariable Long userId,
            @CookieValue(value = "accessToken") String token
    ) {
        Long subscriberId = jwtService.extractUserIdFromAccessToken(token);

        subsService.subscribe(userId, subscriberId);

        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @PostMapping("/{userId}/unsub")
    public ResponseEntity<String> unsubscribeOnAccount(
            @PathVariable Long userId,
            @CookieValue(value = "accessToken") String token
    ) {
        Long unsubscriberId = jwtService.extractUserIdFromAccessToken(token);

        subsService.unsubscribe(userId, unsubscriberId);

        return ResponseEntity.ok("");
    }
}
