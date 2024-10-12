package com.ahitech.factories;

import com.ahitech.dtos.AccountCreationRequest;
import com.ahitech.storage.entities.AccountEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AccountEntityFactory {

    public AccountEntity makeAccountEntity(AccountCreationRequest request) {
        return AccountEntity.builder()
                .userId(request.getUserId())
                .email(request.getEmail())
                .nickname(request.getNickname())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .createAt(new Date())
                .build();
    }
}
