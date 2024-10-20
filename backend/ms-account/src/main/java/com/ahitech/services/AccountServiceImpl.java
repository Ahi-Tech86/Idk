package com.ahitech.services;

import com.ahitech.dtos.*;
import com.ahitech.exception.AppException;
import com.ahitech.factories.AccountDtoFactory;
import com.ahitech.factories.AccountEntityFactory;
import com.ahitech.factories.PrivateAccountDtoFactory;
import com.ahitech.factories.PublicAccountDtoFactory;
import com.ahitech.services.interfaces.AccountService;
import com.ahitech.storage.entities.AccountEntity;
import com.ahitech.storage.enums.Country;
import com.ahitech.storage.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final AccountEntityFactory factory;
    private final AccountDtoFactory dtoFactory;
    private final AccountDataSenderImpl senderService;
    private final PublicAccountDtoFactory publicAccountDtoFactory;
    private final PrivateAccountDtoFactory privateAccountDtoFactory;

    @Override
    public AccountRepresentation getAccount(Long userId, Long requesterId) {
        if (userId.equals(requesterId)) {
            AccountEntity account = isAccountExistsByUserId(userId);

            return publicAccountDtoFactory.makeAccountDto(account);
        }

        AccountEntity account = isAccountExistsByUserId(userId);

        if (account.isPrivate()) {
            return privateAccountDtoFactory.makePrivateAccountDto(account);
        } else {
            return publicAccountDtoFactory.makeAccountDto(account);
        }
    }

    @Override
    @Transactional
    public void createAccount(AccountCreationRequest request) {
        validationAccountCreationRequest(request);

        AccountEntity account = factory.makeAccountEntity(request);

        repository.saveAndFlush(account);
        log.info("The account for the user {} was successfully saved", request.getNickname());
    }

    @Override
    @Transactional
    public AccountDto updateBio(String nickname, UpdateAccountBioRequest request) {
        String bio = request.getBio();

        if (bio == null) {
            log.error("Attempt to update account bio with nullable bio value {}", bio);
            throw new AppException("Bio cannot be null", HttpStatus.BAD_REQUEST);
        }

        if (bio.length() > 300) {
            log.error("Attempt to update bio with large size: {}", bio.length());
            throw new AppException("Bio can't contains more than 300 characters", HttpStatus.BAD_REQUEST);
        }

        AccountEntity account = isAccountExistsByNickname(nickname);
        account.setBio(bio);

        AccountEntity savedAccount = repository.saveAndFlush(account);
        log.info("Successfully bio update for user with nickname {}", nickname);

        return dtoFactory.makeAccountDto(savedAccount);
    }

    @Override
    @Transactional
    public AccountDto updateCity(String nickname, UpdateAccountCityRequest request) {
        String cityName = request.getCity();

        if (cityName == null) {
            log.error("Attempt to update account city with nullable city value {}", cityName);
            throw new AppException("City cannot be null", HttpStatus.BAD_REQUEST);
        }

        if (cityName.length() > 30) {
            log.error("Attempt to update city name with large size: {}", cityName.length());
            throw new AppException("City name can't contains more than 30 characters", HttpStatus.BAD_REQUEST);
        }

        AccountEntity account = isAccountExistsByNickname(nickname);
        account.setCity(cityName);

        AccountEntity savedAccount = repository.saveAndFlush(account);
        log.info("Successfully city name update for user with nickname {}", nickname);

        return dtoFactory.makeAccountDto(savedAccount);
    }

    @Override
    @Transactional
    public AccountDto updatePrivacy(String nickname, UpdateAccountPrivacyRequest request) {
        boolean privacy = request.isPrivacy();

        AccountEntity account = isAccountExistsByNickname(nickname);
        account.setPrivate(privacy);

        AccountEntity savedAccount = repository.saveAndFlush(account);
        log.info("Successfully privacy update for user with nickname {}", nickname);

        return dtoFactory.makeAccountDto(savedAccount);
    }

    @Override
    @Transactional
    public AccountDto updateCountry(String nickname, UpdateAccountCountryRequest request) {
        String countryName = request.getCountry();

        if (countryName == null) {
            log.error("Attempt to update account country with nullable city value {}", countryName);
            throw new AppException("Country cannot be null", HttpStatus.BAD_REQUEST);
        }

        String normalizedCountryName = countryName.trim().toLowerCase();

        Country matchedCountry = null;
        for (Country country : Country.values()) {
            String normalizedEnumName = country.getName().trim().toLowerCase();

            if (
                    normalizedEnumName.equals(normalizedCountryName) ||
                    normalizedEnumName.contains(normalizedCountryName) ||
                    country.name().toLowerCase().equals(normalizedCountryName)
            ) {
                matchedCountry = country;
                break;
            }
        }

        if (matchedCountry == null) {
            log.error("Country {} in not recognized", countryName);
            throw new AppException("Country is not recognized", HttpStatus.BAD_REQUEST);
        }

        AccountEntity account = isAccountExistsByNickname(nickname);
        account.setCountry(matchedCountry);

        AccountEntity savedAccount = repository.saveAndFlush(account);
        log.info("Successfully account country update for user with nickname {}", nickname);

        return dtoFactory.makeAccountDto(savedAccount);
    }

    @Override
    @Transactional
    public AccountDto updateWebsite(String nickname, UpdateAccountWebsiteRequest request) {
        String website = request.getWebsite();

        if (website == null) {
            log.error("Attempt to update account website with nullable website value {}", website);
            throw new AppException("City cannot be null", HttpStatus.BAD_REQUEST);
        }

        if (website.length() > 75) {
            log.error("Attempt to update website name with large size: {}", website.length());
            throw new AppException("Website name can't contains more than 75 characters", HttpStatus.BAD_REQUEST);
        }

        AccountEntity account = isAccountExistsByNickname(nickname);
        account.setWebsite(website);

        AccountEntity savedAccount = repository.saveAndFlush(account);
        log.info("Successfully website name update for user with nickname {}", nickname);

        return dtoFactory.makeAccountDto(savedAccount);
    }

    @Override
    @Transactional
    public AccountDto updateFullname(String nickname, UpdateAccountFullnameRequest request) {
        String firstname = request.getFirstname();
        String lastname = request.getLastname();

        AccountEntity account = isAccountExistsByNickname(nickname);

        if (firstname != null) {
            account.setFirstname(firstname);
        }

        if (lastname != null) {
            account.setLastname(lastname);
        }

        if (firstname == null && lastname == null) {
            log.error("Attempt to update account fullname with nullable firstname value {} and lastname value {}", firstname, lastname);
            throw new AppException("Fullname cannot be null", HttpStatus.BAD_REQUEST);
        }

        AccountEntity savedAccount = repository.saveAndFlush(account);
        log.info("Successfully fullname update for user with nickname {}", nickname);

        senderService.sendMessage(nickname, request);

        return dtoFactory.makeAccountDto(savedAccount);
    }

    private AccountEntity isAccountExistsByUserId(Long userId) {
        Optional<AccountEntity> optionalAccount = repository.findByUserId(userId);

        if (optionalAccount.isEmpty()) {
            log.error("An attempt to perform any actions with a non-existent userId: {}", userId);
            throw new AppException(
                    String.format("Account with %s userId doesn't exists", userId), HttpStatus.BAD_REQUEST
            );
        } else {
            return optionalAccount.get();
        }
    }

    private AccountEntity isAccountExistsByNickname(String nickname) {
        Optional<AccountEntity> optionalAccount = repository.findByNickname(nickname);

        if (optionalAccount.isEmpty()) {
            log.error("An attempt to perform any actions with a non-existent nickname: {}", nickname);
            throw new AppException(
                    String.format("Account with %s nickname doesn't exists", nickname), HttpStatus.BAD_REQUEST
            );
        } else {
            return optionalAccount.get();
        }
    }

    private void validationAccountCreationRequest(AccountCreationRequest request) {
        Long userId = request.getUserId();
        String email = request.getEmail();
        String nickname = request.getNickname();
        String firstname = request.getFirstname();
        String lastname = request.getLastname();

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
