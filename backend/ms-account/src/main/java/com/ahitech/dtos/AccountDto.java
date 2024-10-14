package com.ahitech.dtos;

import com.ahitech.storage.enums.Country;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private String nickname;
    private String firstname;
    private String lastname;
    private String bio;
    @JsonProperty("create_at")
    private String createAt;
    @JsonProperty("is_private")
    private boolean isPrivate;
    private Long followersCount;
    private Long followingCount;
    private Country country;
    private String city;
    private String website;
}
