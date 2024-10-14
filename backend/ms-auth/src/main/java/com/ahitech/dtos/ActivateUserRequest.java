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
public class ActivateUserRequest {
    @Schema(description = "User's email", example = "example@mail.com")
    private String email;
    @JsonProperty("activation_code")
    @Schema(description = "The activation code that was sent to the user's email", example = "123456")
    private String activationCode;
}
