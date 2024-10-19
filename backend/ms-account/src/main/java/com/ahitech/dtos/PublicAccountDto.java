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
public class PublicAccountDto implements AccountRepresentation {
    private String nickname;
    private String firstname;
    private String lastname;
    private String bio;
    @JsonProperty("is_private")
    private boolean isPrivate;
    private Long followersCount;
    private Long followingCount;
    private Country country;
    private String city;
    private String website;
}
