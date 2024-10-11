package com.ahitech.services;

import com.ahitech.dtos.ChangePasswordRequest;
import com.ahitech.dtos.PasswordResetConfirmRequest;
import com.ahitech.dtos.PasswordResetRequest;
import com.ahitech.exception.AppException;
import com.ahitech.services.interfaces.PasswordService;
import com.ahitech.storage.entities.UserEntity;
import com.ahitech.storage.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PasswordServiceImpl implements PasswordService {

    private final UserRepository repository;
    private final EmailServiceImpl emailService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    public PasswordServiceImpl(
            @Qualifier("customStringRedisTemplate") RedisTemplate<String, String> stringRedisTemplate,
            PasswordEncoder passwordEncoder,
            EmailServiceImpl emailService,
            UserRepository userRepository
    ) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.repository = userRepository;
    }

    @Override
    @Transactional
    public void changeUserPassword(ChangePasswordRequest request) {
        String email = request.getEmail();
        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();

        isPasswordNullOrEmpty(newPassword);

        // getting user from db
        UserEntity user = isUserExistsByEmail(email);

        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            repository.saveAndFlush(user);
            log.info("Password for user with email {} was successfully changed", email);
        } else {
            log.error("Attempt to log with incorrect password to {} account", email);
            throw new AppException(
                    String.format("Incorrect password for user with email %s", email), HttpStatus.UNAUTHORIZED
            );
        }
    }

    @Override
    public void sendPasswordResetCode(PasswordResetRequest request) {
        String email = request.getEmail();
        isUserExistsByEmail(email);

        String resetPasswordCode = generatePasswordResetCode();
        String emailKey = "reset_password:" + email;
        stringRedisTemplate.opsForValue().set(emailKey, resetPasswordCode, 15, TimeUnit.MINUTES);

        try {
            emailService.sendActivationCodeToEmail(email, resetPasswordCode, "Reset password code");
            log.info("Message with reset password code was send to email {}", email);
        } catch (RuntimeException exception) {
            log.error("Attempt to send message was unsuccessful", exception);
            throw new AppException("There was an error sending the message", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void confirmPasswordReset(PasswordResetConfirmRequest request) {
        String email = request.getEmail();
        String code = request.getResetCode();
        String password = request.getPassword();

        UserEntity user = isUserExistsByEmail(email);
        isPasswordNullOrEmpty(password);

        String emailKey = "reset_password:" + email;
        String resetCode = stringRedisTemplate.opsForValue().get(emailKey);

        if (!resetCode.equals(code)) {
            log.error("Attempting to reset password an account with an incorrect reset code to email {}", email);
            throw new AppException("The code for reset password doesn't match what the server generated", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(password));
        repository.saveAndFlush(user);
        log.info("Password for user with email {} was successfully reset and saved new", email);
    }

    private String generatePasswordResetCode() {
        String symbols = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        StringBuilder result = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 16; i++) {
            int randomIndex = random.nextInt(symbols.length());
            char randomChar = symbols.charAt(randomIndex);
            result.append(randomChar);
        }

        return result.toString();
    }

    private void isPasswordNullOrEmpty(String password) {
        if (password == null || password.isEmpty()) {
            log.error("Attempt to change password with invalid value");
            throw new AppException("Password can't be empty or null", HttpStatus.BAD_REQUEST);
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
