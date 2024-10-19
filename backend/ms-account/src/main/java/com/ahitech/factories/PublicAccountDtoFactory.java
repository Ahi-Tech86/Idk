package com.ahitech.factories;

import com.ahitech.dtos.PublicAccountDto;
import com.ahitech.storage.entities.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class PublicAccountDtoFactory {
    public PublicAccountDto makeAccountDto(AccountEntity entity) {
        return PublicAccountDto.builder()
                .nickname(entity.getNickname())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .bio(entity.getBio())
                .isPrivate(entity.isPrivate())
                .followersCount(entity.getFollowersCount())
                .followingCount(entity.getFollowingCount())
                .country(entity.getCountry())
                .city(entity.getCity())
                .website(entity.getWebsite())
                .build();
    }
}
