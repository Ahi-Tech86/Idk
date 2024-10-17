package com.ahitech.services.interfaces;

import com.ahitech.dtos.FullnameUpdateRequest;

public interface UserService {
    void updateFullname(FullnameUpdateRequest requestBody);
}
