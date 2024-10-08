package com.ahitech.services;

import com.ahitech.storage.entities.UserEntity;
import com.ahitech.storage.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    @Value("${application.security.jwt.refresh-token.expiration}")
    private Long refreshTokenExpirationTime;

    private final TokenRepository repository;

    @Override
    public void createAndSaveToken(UserEntity user) {

    }

    @Override
    public String getTokenByUserEmail(String email) {
        return "";
    }
}
