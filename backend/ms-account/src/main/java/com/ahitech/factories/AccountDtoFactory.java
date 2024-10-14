package com.ahitech.factories;

import com.ahitech.dtos.AccountDto;
import com.ahitech.storage.entities.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountDtoFactory {
    public AccountDto makeAccountDto(AccountEntity entity) {
        return AccountDto.builder()
                .nickname(entity.getNickname())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .bio(entity.getBio())
                .createAt(entity.getCreateAt().toString())
                .isPrivate(entity.isPrivate())
                .followersCount(entity.getFollowersCount())
                .followingCount(entity.getFollowingCount())
                .country(entity.getCountry())
                .city(entity.getCity())
                .website(entity.getWebsite())
                .build();
    }
}
