package com.ahitech.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMessageDto {
    @JsonProperty("user_id")
    private Long userId;
    private String email;
    private String nickname;
    private String firstname;
    private String lastname;
}
