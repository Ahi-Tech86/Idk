package com.ahitech.dtos;

import com.ahitech.storage.enums.Country;

public interface AccountRepresentation {
    String getNickname();
    boolean isPrivate();
    Long getFollowersCount();
    Long getFollowingCount();
    Country getCountry();
}
