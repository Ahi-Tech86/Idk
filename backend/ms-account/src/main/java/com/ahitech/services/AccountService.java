package com.ahitech.services;

import com.ahitech.dtos.AccountCreationRequest;

public interface AccountService {
    void createAccount(AccountCreationRequest request);
}
