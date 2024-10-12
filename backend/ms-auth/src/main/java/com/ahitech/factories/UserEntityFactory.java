package com.ahitech.factories;

import com.ahitech.dtos.TemporaryUserDto;
import com.ahitech.storage.entities.UserEntity;
import org.springframework.stereotype.Component;

import static com.ahitech.enums.AppRole.USER;

@Component
public class UserEntityFactory {

    public UserEntity makeUserEntity(TemporaryUserDto userDto) {
        return UserEntity.builder()
                .email(userDto.getEmail())
                .nickname(userDto.getNickname())
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .role(USER)
                .build();
    }
}
