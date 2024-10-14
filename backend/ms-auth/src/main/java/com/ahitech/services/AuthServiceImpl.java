package com.ahitech.services;

import com.ahitech.dtos.*;
import com.ahitech.enums.AppRole;
import com.ahitech.exception.AppException;
import com.ahitech.factories.TemporaryUserDtoFactory;
import com.ahitech.factories.UserDtoFactory;
import com.ahitech.factories.UserEntityFactory;
import com.ahitech.services.interfaces.AuthService;
import com.ahitech.storage.entities.UserEntity;
import com.ahitech.storage.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cache.internal.NaturalIdCacheKey;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtServiceImpl jwtService;
    private final UserRepository repository;
    private final TokenServiceImpl tokenService;
    private final EmailServiceImpl emailService;
    private final UserDtoFactory userDtoFactory;
    private final PasswordEncoder passwordEncoder;
    private final UserEntityFactory userEntityFactory;
    private final TemporaryUserDtoFactory temporaryUserDtoFactory;

    private final RedisTemplate<String, TemporaryUserDto> redisTemplate;

    @Override
    public String register(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String nickname = signUpRequest.getNickname();

        // checking email on pattern
        if (!isEmailValid(email)) {
            log.error("Attempt register with invalid email {}", email);
            throw new AppException("Email is invalid", HttpStatus.BAD_REQUEST);
        }

        isEmailUniqueness(email);
        isNicknameUniqueness(nickname);

        String activationCode = generateActivationCode();

        // saving information about user temporary in redis
        TemporaryUserDto temporaryUserDto = temporaryUserDtoFactory.makeTemporaryUserDto(signUpRequest, activationCode);
        redisTemplate.opsForValue().set(email, temporaryUserDto, 20, TimeUnit.MINUTES);
        log.info("User information with email {} is temporarily saved", email);

        try {
            emailService.sendActivationCodeToEmail(email, activationCode, "Account activation");
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
    public List<Object> activate(ActivateUserRequest activateUserRequest) {
        List<Object> response = new ArrayList<>();

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

        tokenService.createAndSaveToken(user);
        log.info("Refresh token for {} user was successfully saved", email);

        response.add(userDtoFactory.makeUserDto(savedUser));
        response.add(user.getId());

        return response;
    }

    @Override
    public List<Object> login(SignInRequest signInRequest) {
        // create response list
        List<Object> response = new ArrayList<>();
        String email = signInRequest.getEmail();

        // getting entity from db if user exists
        UserEntity user = isUserExistsByEmail(email);
        String nickname = user.getNickname();

        // matches password from request and password from db
        if (passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            Long userId = user.getId();
            AppRole userRole = user.getRole();
            UserDto userDto = userDtoFactory.makeUserDto(user);

            String accessToken = jwtService.generateAccessToken(userId, nickname, userRole);
            String refreshToken = jwtService.generateRefreshToken(userId, nickname, userRole);

            response.add(userDto);
            response.add(accessToken);
            response.add(refreshToken);

            log.info("Successful login to {} account", email);
            return response;
        } else {
            log.error("Attempt to log with incorrect password to {} account", email);
            throw new AppException(
                    String.format("Incorrect password for user with email %s", email), HttpStatus.UNAUTHORIZED
            );
        }
    }

    private String generateActivationCode() {
        Random random = new Random();

        int number = 1 + random.nextInt(1000000);

        return String.format("%6d", number);
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

    private void isNicknameUniqueness(String nickname) {
        Optional<UserEntity> optionalUser = repository.findByNickname(nickname);

        if (optionalUser.isPresent()) {
            log.error("Attempt to with an existing nickname {}", nickname);
            throw new AppException(String.format("User with nickname %s is already exists", nickname), HttpStatus.BAD_REQUEST);
        }
    }

    private UserEntity isUserExistsByEmail(String email) {
        Optional<UserEntity> optionalUser = repository.findByEmail(email);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            log.error("Attempt to log into an account with non-existent email {}", email);
            throw new AppException(String.format("User with email %s doesn't exists", email), HttpStatus.NOT_FOUND);
        }
    }
}
