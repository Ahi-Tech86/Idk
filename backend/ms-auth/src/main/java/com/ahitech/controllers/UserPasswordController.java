package com.ahitech.controllers;

import com.ahitech.dtos.ChangePasswordRequest;
import com.ahitech.dtos.PasswordResetConfirmRequest;
import com.ahitech.dtos.PasswordResetRequest;
import com.ahitech.services.PasswordServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/password")
//@Api(value = "Password operations in auth microservice", tags = "password")
public class UserPasswordController {

    private final PasswordServiceImpl service;

    @PostMapping("/change")
    @Operation(
            summary = "Changing password",
            description = "Allows a user to change their password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password was successfully changed"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                    @ApiResponse(responseCode = "401", description = "Password doesn't match with user password"),
                    @ApiResponse(responseCode = "404", description = "User with email in request doesn't exists")
            }
    )
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        service.changeUserPassword(request);

        return ResponseEntity.ok("Password was successfully changed");
    }

    @PostMapping("/sendResetCode")
    @Operation(
            summary = "Sending reset code on email",
            description = "Receiving user email and send on user's email message with reset code",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reset code was successful send on email"),
                    @ApiResponse(responseCode = "404", description = "User with email in request doesn't exists"),
                    @ApiResponse(responseCode = "500", description = "An error occurred while sending message an email")
            }
    )
    public ResponseEntity<String> sendCode(@RequestBody PasswordResetRequest request) {
        service.sendPasswordResetCode(request);

        return ResponseEntity.ok("An reset password code has been sent to your email, please send the reset password code before it expires. " +
                "The reset password code expires in 15 minutes.");
    }

    @PostMapping("/confirmPasswordReset")
    @Operation(
            summary = "Password reset",
            description = "Confirm password reset and successful password update",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful password reset"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                    @ApiResponse(responseCode = "404", description = "User with email in request doesn't exists")
            }
    )
    public ResponseEntity<String> confirmResetPassword(@RequestBody PasswordResetConfirmRequest request) {
        service.confirmPasswordReset(request);

        return ResponseEntity.ok("Your password was successfully reset and created new");
    }
}
