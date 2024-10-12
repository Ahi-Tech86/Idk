package com.ahitech.services;

import com.ahitech.dtos.AccountCreationRequest;
import com.ahitech.exception.AppException;
import com.ahitech.factories.AccountEntityFactory;
import com.ahitech.storage.entities.AccountEntity;
import com.ahitech.storage.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final AccountEntityFactory factory;

    @Override
    @Transactional
    public void createAccount(AccountCreationRequest request) {
        validationAccountCreationRequest(request);

        AccountEntity account = factory.makeAccountEntity(request);

        repository.saveAndFlush(account);
        log.info("The account for the user {} was successfully saved", request.getNickname());
    }

    private void validationAccountCreationRequest(AccountCreationRequest request) {
        Long userId = request.getUserId();
        String email = request.getEmail();
        String nickname = request.getNickname();
        String firstname = request.getFirstname();
        String lastname = request.getLastname();

        if (request == null) {
            log.error("Request data was lost");
            throw new AppException("Request data was lost", HttpStatus.BAD_REQUEST);
        }

        if (userId == null || userId == 0) {
            log.error("The request contained an zero or nullable userId: {}", userId);
            throw new AppException("UserId can't be zero or null", HttpStatus.BAD_REQUEST);
        }

        if (email == null || email.isEmpty()) {
            log.error("The request contained an empty or nullable email: {}", email);
            throw new AppException("Email can't be empty or null", HttpStatus.BAD_REQUEST);
        }

        if (nickname == null || nickname.isEmpty()) {
            log.error("The request contained an empty or nullable nickname: {}", nickname);
            throw new AppException("Nickname can't be empty or null", HttpStatus.BAD_REQUEST);
        }

        if (firstname == null || firstname.isEmpty()) {
            log.error("The request contained an empty or nullable firstname: {}", firstname);
            throw new AppException("Firstname can't be empty or null", HttpStatus.BAD_REQUEST);
        }

        if (lastname == null || lastname.isEmpty()) {
            log.error("The request contained an empty or nullable lastname: {}", lastname);
            throw new AppException("Lastname can't be empty or null", HttpStatus.BAD_REQUEST);
        }
    }
}
