package com.ahitech.services;

import com.ahitech.dtos.FullnameUpdateRequest;
import com.ahitech.exception.AppException;
import com.ahitech.services.interfaces.UserService;
import com.ahitech.storage.entities.UserEntity;
import com.ahitech.storage.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public void updateFullname(FullnameUpdateRequest requestBody) {
        String nickname = requestBody.getNickname();
        String firstname = requestBody.getFirstname();
        String lastname = requestBody.getLastname();

        UserEntity user = isUserExistsByNickname(nickname);
        user.setFirstname(firstname);
        user.setLastname(lastname);

        repository.saveAndFlush(user);
        log.info("Fullname for user {} was successfully updated", nickname);
    }

    private UserEntity isUserExistsByNickname(String nickname) {
        Optional<UserEntity> optionalUser = repository.findByNickname(nickname);

        if (optionalUser.isEmpty()) {
            log.error("An attempt to perform any actions with a non-existent nickname: {}", nickname);
            throw new AppException(
                    String.format("User with %s nickname doesn't exists", nickname), HttpStatus.BAD_REQUEST
            );
        } else {
            return optionalUser.get();
        }
    }
}
