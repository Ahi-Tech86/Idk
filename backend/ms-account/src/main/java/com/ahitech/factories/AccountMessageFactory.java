package com.ahitech.factories;

import com.ahitech.dtos.AccountMessage;
import com.ahitech.dtos.UpdateAccountFullnameRequest;
import org.springframework.stereotype.Component;

@Component
public class AccountMessageFactory {
    public AccountMessage makeAccountMessage(String nickname, UpdateAccountFullnameRequest requestBody) {
        return AccountMessage.builder()
                .nickname(nickname)
                .firstname(requestBody.getFirstname())
                .lastname(requestBody.getLastname())
                .build();
    }
}
