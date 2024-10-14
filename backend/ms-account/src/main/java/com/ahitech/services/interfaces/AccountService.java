package com.ahitech.services.interfaces;

import com.ahitech.dtos.*;

public interface AccountService {
    void createAccount(AccountCreationRequest request);
    AccountDto updateBio(String nickname, UpdateAccountBioRequest request);
    AccountDto updateCity(String nickname, UpdateAccountCityRequest request);
    AccountDto updatePrivacy(String nickname, UpdateAccountPrivacyRequest request);
    AccountDto updateCountry(String nickname, UpdateAccountCountryRequest request);
    AccountDto updateWebsite(String nickname, UpdateAccountWebsiteRequest request);
}
