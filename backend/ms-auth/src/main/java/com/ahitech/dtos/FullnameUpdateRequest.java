package com.ahitech.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullnameUpdateRequest {
    private String nickname;
    private String firstname;
    private String lastname;
}
