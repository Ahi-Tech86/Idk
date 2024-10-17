package com.ahitech.controllers;

import com.ahitech.dtos.*;
import com.ahitech.services.AccountServiceImpl;
import com.ahitech.services.JwtServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

    private final JwtServiceImpl jwtService;
    private final AccountServiceImpl service;

    @PostMapping("/updateBio")
    @Operation(
            summary = "Update account bio",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful account bio update"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    public ResponseEntity<AccountDto> updateBio(
            @CookieValue(value = "accessToken") String token,
            @RequestBody UpdateAccountBioRequest requestBody
    ) {
        String nickname = jwtService.extractEmailFromAccessToken(token);

        return ResponseEntity.ok(service.updateBio(nickname, requestBody));
    }

    @PostMapping("/updateCity")
    @Operation(
            summary = "Update account city",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful account city update"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    public ResponseEntity<AccountDto> updateCity(
            @CookieValue(value = "accessToken") String token,
            @RequestBody UpdateAccountCityRequest requestBody
    ) {
        String nickname = jwtService.extractEmailFromAccessToken(token);

        return ResponseEntity.ok(service.updateCity(nickname, requestBody));
    }

    @PostMapping("/updatePrivacy")
    @Operation(
            summary = "Update account privacy",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful account privacy update"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    public ResponseEntity<AccountDto> updatePrivacy(
            @CookieValue(value = "accessToken") String token,
            @RequestBody UpdateAccountPrivacyRequest requestBody
    ) {
        String nickname = jwtService.extractEmailFromAccessToken(token);

        return ResponseEntity.ok(service.updatePrivacy(nickname, requestBody));
    }

    @PostMapping("/updateCountry")
    @Operation(
            summary = "Update account country",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful account country update"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    public ResponseEntity<AccountDto> updateCountry(
            @CookieValue(value = "accessToken") String token,
            @RequestBody UpdateAccountCountryRequest requestBody
    ) {
        String nickname = jwtService.extractEmailFromAccessToken(token);

        return ResponseEntity.ok(service.updateCountry(nickname, requestBody));
    }

    @PostMapping("/updateWebsite")
    @Operation(
            summary = "Update account website",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful account website update"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    public ResponseEntity<AccountDto> updateWebsite(
            @CookieValue(value = "accessToken") String token,
            @RequestBody UpdateAccountWebsiteRequest requestBody
    ) {
        String nickname = jwtService.extractEmailFromAccessToken(token);

        return ResponseEntity.ok(service.updateWebsite(nickname, requestBody));
    }

    @PostMapping("/updateFullname")
    @Operation(
            summary = "Update account fullname",
            description = "Update account fullname and update fullname in auth microservice",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful account fullname update"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                    @ApiResponse(responseCode = "500", description = "Error while serializing message")
            }
    )
    public ResponseEntity<AccountDto> updateFullname(
            @CookieValue(value = "accessToken") String token,
            @RequestBody UpdateAccountFullnameRequest requestBody
    ) {
        String nickname = jwtService.extractEmailFromAccessToken(token);

        return ResponseEntity.ok(service.updateFullname(nickname, requestBody));
    }
}
