package com.ahitech.services;

import com.ahitech.storage.entities.UserEntity;

public interface TokenService {
    void createAndSaveToken(UserEntity user);
    String getTokenByUserEmail(String email);
}
