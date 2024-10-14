package com.ahitech.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @Schema(description = "User's email", example = "example@mail.com")
    private String email;
    @Schema(description = "User nickname in the application", example = "UserNickname007")
    private String nickname;
    @Schema(description = "User's firstname", example = "Firstname")
    private String firstname;
    @Schema(description = "User's firstname", example = "Lastname")
    private String lastname;
    @Schema(description = "User's password", example = "password")
    private String password;
}
