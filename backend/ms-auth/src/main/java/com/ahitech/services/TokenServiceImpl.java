package com.ahitech.services;

import com.ahitech.enums.AppRole;
import com.ahitech.exception.AppException;
import com.ahitech.factories.RefreshTokenEntityFactory;
import com.ahitech.services.interfaces.TokenService;
import com.ahitech.storage.entities.RefreshTokenEntity;
import com.ahitech.storage.entities.UserEntity;
import com.ahitech.storage.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    @Value("${application.security.jwt.refresh-token.expiration}")
    private Long refreshTokenExpirationTime;

    private final JwtServiceImpl jwtService;
    private final TokenRepository repository;
    private final RefreshTokenEntityFactory factory;
    private final EncryptionServiceImpl encryptionService;


    @Override
    @Transactional
    public void createAndSaveToken(UserEntity user) {
        Long id = user.getId();
        String email = user.getEmail();
        AppRole role = user.getRole();

        // generating refresh token and extract expiration time
        String refreshToken = jwtService.generateRefreshToken(id ,email, role);
        Date expirationTime = new Date(System.currentTimeMillis() + refreshTokenExpirationTime);

        // encrypting refresh token before save in db
        String encryptedToken = encryptionService.encrypt(refreshToken);

        // mapping to entity and saving in db
        RefreshTokenEntity refreshTokenEntity = factory.makeRefreshTokenEntity(user, encryptedToken, expirationTime);
        repository.saveAndFlush(refreshTokenEntity);
    }

    @Override
    @Transactional
    public String getTokenByUserEmail(String email) {
        Logger logger = LoggerFactory.getLogger(getClass());

        try {
            RefreshTokenEntity refreshTokenEntity = repository.findByEmail(email).orElseThrow(
                    () -> new AppException(
                            String.format("Token for user with email %s doesn't exists", email), HttpStatus.NOT_FOUND
                    )
            );

            String encryptedToken = refreshTokenEntity.getToken();
            String decryptedToken = encryptionService.decrypt(encryptedToken);

            if (jwtService.isRefreshTokenExpired(decryptedToken)) {
                String newRefreshToken = jwtService.generateRefreshToken(
                        jwtService.extractUserIdFromAccessToken(decryptedToken),
                        email,
                        jwtService.extractRoleFromAccessToken(decryptedToken)
                );
                String encryptedNewToken = encryptionService.encrypt(newRefreshToken);
                refreshTokenEntity.setToken(encryptedNewToken);
                refreshTokenEntity.setCreateAt(new Date(System.currentTimeMillis()));
                refreshTokenEntity.setExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpirationTime));
                repository.saveAndFlush(refreshTokenEntity);
                log.info("Refresh token for user {} was successfully updated", email);

                return newRefreshToken;
            }

            return decryptedToken;
        } catch (AppException exception) {
            logger.error("Error when receiving a token for a user with email {}: {}", email, exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            logger.error("An unexpected error occurred {}", exception.getMessage());
            throw new AppException("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
