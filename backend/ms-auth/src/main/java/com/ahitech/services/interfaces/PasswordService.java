package com.ahitech.services.interfaces;

import com.ahitech.dtos.ChangePasswordRequest;
import com.ahitech.dtos.PasswordResetConfirmRequest;
import com.ahitech.dtos.PasswordResetRequest;

public interface PasswordService {
    void changeUserPassword(ChangePasswordRequest request);
    void sendPasswordResetCode(PasswordResetRequest request);
    void confirmPasswordReset(PasswordResetConfirmRequest request);
}
