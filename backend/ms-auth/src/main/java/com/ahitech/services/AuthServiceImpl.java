package com.ahitech.services;

import com.ahitech.dtos.UserDto;
import com.ahitech.dtos.SignInRequest;
import com.ahitech.dtos.SignUpRequest;
import com.ahitech.dtos.ActivateUserRequest;
import com.ahitech.exception.AppException;
import com.ahitech.storage.entities.UserEntity;
import com.ahitech.storage.entities.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;

    @Override
    public String register(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();

        // checking email on pattern
        if (!isEmailValid(email)) {
            log.error("Attempt register with invalid email {}", email);
            throw new AppException("Email is invalid", HttpStatus.BAD_REQUEST);
        }

        isEmailUniqueness(email);

        String activationCode = generateActivationCode();

        return "";
    }

    @Override
    public UserDto activate(ActivateUserRequest activateUserRequest) {
        return null;
    }

    @Override
    public UserDto login(SignInRequest signInRequest) {
        return null;
    }

    private boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);

        if (email == null) {
            return false;
        }

        return pattern.matcher(email).matches();
    }

    private void isEmailUniqueness(String email) {

        Optional<UserEntity> optionalUser = repository.findByEmail(email);

        if (optionalUser.isPresent()) {
            log.error("Attempt to register with an existing email {}", email);
            throw new AppException(String.format("User with email %s is already exists", email), HttpStatus.BAD_REQUEST);
        }
    }

    private String generateActivationCode() {
        Random random = new Random();

        int number = 1 + random.nextInt(1000000);

        return String.format("%6d", number);
    }
}
