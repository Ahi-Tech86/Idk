package com.ahitech.factories;

import com.ahitech.dtos.SignUpRequest;
import com.ahitech.dtos.TemporaryUserDto;
import org.springframework.stereotype.Component;

@Component
public class TemporaryUserDtoFactory {

    public TemporaryUserDto makeTemporaryUserDto(SignUpRequest request, String activationCode) {
        return TemporaryUserDto.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .password(request.getPassword())
                .activationCode(activationCode)
                .build();
    }
}
