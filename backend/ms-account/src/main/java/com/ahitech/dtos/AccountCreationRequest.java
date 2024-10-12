package com.ahitech.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreationRequest {
    private Long userId;
    private String email;
    private String nickname;
    private String firstname;
    private String lastname;
}
