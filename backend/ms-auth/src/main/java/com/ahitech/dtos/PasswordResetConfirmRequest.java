package com.ahitech.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetConfirmRequest {
    @Schema(description = "User's email", example = "example@mail.com")
    private String email;
    @JsonProperty("reset_code")
    @Schema(description = "The reset password code that was sent to the user's email", example = "1p2a3s4s5w6o7r8d")
    private String resetCode;
    @Schema(description = "New password for user", example = "new_password")
    private String password;
}
