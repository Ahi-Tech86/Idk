package com.ahitech.services.interfaces;

import com.ahitech.dtos.UserDto;

public interface UserAccountMessagingService {
    void sendUserData(UserDto userDto, Long userId);
}
