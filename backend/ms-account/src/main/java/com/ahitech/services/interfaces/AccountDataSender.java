package com.ahitech.services.interfaces;

import com.ahitech.dtos.UpdateAccountFullnameRequest;

public interface AccountDataSender {
    void sendMessage(String nickname, UpdateAccountFullnameRequest request);
}
