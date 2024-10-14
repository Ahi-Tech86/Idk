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
public class ChangePasswordRequest {
    @Schema(description = "User's email", example = "example@mail.com")
    private String email;
    @JsonProperty("old_password")
    @Schema(description = "Current password of the user", example = "password")
    private String oldPassword;
    @JsonProperty("new_password")
    @Schema(description = "New password of the user", example = "new_password")
    private String newPassword;
}
