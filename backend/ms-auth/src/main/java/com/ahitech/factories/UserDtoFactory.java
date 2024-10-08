package com.ahitech.factories;

import com.ahitech.dtos.UserDto;
import com.ahitech.storage.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDtoFactory {

    public UserDto makeUserDto(UserEntity entity) {
        return UserDto.builder()
                .email(entity.getEmail())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .build();
    }
}
