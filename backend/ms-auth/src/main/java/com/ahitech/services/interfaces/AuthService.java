package com.ahitech.services.interfaces;

import com.ahitech.dtos.ActivateUserRequest;
import com.ahitech.dtos.SignInRequest;
import com.ahitech.dtos.SignUpRequest;
import com.ahitech.dtos.UserDto;

public interface AuthService {
    String register(SignUpRequest signUpRequest);
    UserDto activate(ActivateUserRequest activateUserRequest);
    UserDto login(SignInRequest signInRequest);
}
