package com.ahitech.controllers;

import com.ahitech.dtos.ChangePasswordRequest;
import com.ahitech.dtos.PasswordResetConfirmRequest;
import com.ahitech.dtos.PasswordResetRequest;
import com.ahitech.services.PasswordServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/password")
public class UserPasswordController {

    private final PasswordServiceImpl service;

    @PostMapping("/change")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        service.changeUserPassword(request);

        return ResponseEntity.ok("Password was successfully changed");
    }

    @PostMapping("/sendResetCode")
    public ResponseEntity<String> sendCode(@RequestBody PasswordResetRequest request) {
        service.sendPasswordResetCode(request);

        return ResponseEntity.ok("An reset password code has been sent to your email, please send the reset password code before it expires. " +
                "The reset password code expires in 15 minutes.");
    }

    @PostMapping("/confirmPasswordReset")
    public ResponseEntity<String> confirmResetPassword(@RequestBody PasswordResetConfirmRequest request) {
        service.confirmPasswordReset(request);

        return ResponseEntity.ok("Your password was successfully reset and created new");
    }
}
