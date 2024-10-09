package com.ahitech.services.interfaces;

import com.ahitech.storage.entities.UserEntity;

public interface TokenService {
    void createAndSaveToken(UserEntity user);
    String getTokenByUserEmail(String email);
}
