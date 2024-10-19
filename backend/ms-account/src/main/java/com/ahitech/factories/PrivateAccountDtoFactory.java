package com.ahitech.factories;

import com.ahitech.dtos.PrivateAccountDto;
import com.ahitech.storage.entities.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class PrivateAccountDtoFactory {
    public PrivateAccountDto makePrivateAccountDto(AccountEntity entity) {
        return PrivateAccountDto.builder()
                .nickname(entity.getNickname())
                .isPrivate(entity.isPrivate())
                .followersCount(entity.getFollowersCount())
                .followingCount(entity.getFollowingCount())
                .country(entity.getCountry())
                .build();
    }
}
