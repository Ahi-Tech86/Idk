package com.ahitech.factories;

import com.ahitech.dtos.UserDto;
import com.ahitech.dtos.UserMessageDto;
import org.springframework.stereotype.Component;

@Component
public class UserMessageDtoFactory {

    public UserMessageDto makeUserMessageDto(UserDto userDto, Long userId) {
        return UserMessageDto.builder()
                .userId(userId)
                .email(userDto.getEmail())
                .nickname(userDto.getNickname())
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .build();
    }
}
