package com.ahitech.services.interfaces;

import com.ahitech.dtos.ActivateUserRequest;
import com.ahitech.dtos.SignInRequest;
import com.ahitech.dtos.SignUpRequest;
import com.ahitech.dtos.UserDto;

import java.util.List;

public interface AuthService {
    String register(SignUpRequest signUpRequest);
    List<Object> activate(ActivateUserRequest activateUserRequest);
    List<Object> login(SignInRequest signInRequest);
}
