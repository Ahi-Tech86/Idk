package com.ahitech.services;

import com.ahitech.dtos.*;
import com.ahitech.exception.AppException;
import com.ahitech.factories.TemporaryUserDtoFactory;
import com.ahitech.factories.UserDtoFactory;
import com.ahitech.factories.UserEntityFactory;
import com.ahitech.storage.entities.UserEntity;
import com.ahitech.storage.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final EmailServiceImpl emailService;
    private final UserDtoFactory userDtoFactory;
    private final PasswordEncoder passwordEncoder;
    private final UserEntityFactory userEntityFactory;
    private final TemporaryUserDtoFactory temporaryUserDtoFactory;

    private final RedisTemplate<String, TemporaryUserDto> redisTemplate;

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

        // saving information about user temporary in redis
        TemporaryUserDto temporaryUserDto = temporaryUserDtoFactory.makeTemporaryUserDto(signUpRequest, activationCode);
        redisTemplate.opsForValue().set(email, temporaryUserDto, 20, TimeUnit.MINUTES);
        log.error("User information with email {} is temporarily saved", email);

        try {
            emailService.sendActivationCodeToEmail(email, activationCode);
            log.info("Message with activation code was send to email {}", email);
        } catch (RuntimeException exception) {
            log.error("Attempt to send message was unsuccessful", exception);
            throw new AppException("There was an error sending the message", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return "An activation code has been sent to your email, please send the activation code before it expires. " +
                "The activation code expires in 20 minutes.";
    }

    @Override
    @Transactional
    public UserDto activate(ActivateUserRequest activateUserRequest) {
        String email = activateUserRequest.getEmail();
        String activationCode = activateUserRequest.getActivationCode();

        // getting temporary saved information about user
        TemporaryUserDto temporaryUserDto = redisTemplate.opsForValue().get(email);

        // comparison of generated code and code to sent by the user
        if (!activationCode.equals(temporaryUserDto.getActivationCode())) {
            log.error("Attempting to activate an account with an incorrect activation code to email {}", email);
            throw new AppException("The activation code doesn't match what the server generated", HttpStatus.UNAUTHORIZED);
        }

        // mapping entity and encoding password
        UserEntity user = userEntityFactory.makeUserEntity(temporaryUserDto);
        user.setPassword(passwordEncoder.encode(temporaryUserDto.getPassword()));

        UserEntity savedUser = repository.saveAndFlush(user);
        log.info("User with email {} was successfully saved", email);

        return userDtoFactory.makeUserDto(savedUser);
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
