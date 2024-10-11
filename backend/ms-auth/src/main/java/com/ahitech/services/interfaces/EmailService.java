package com.ahitech.services.interfaces;

public interface EmailService {
    void sendActivationCodeToEmail(String to, String activationCode, String titleMessage);
}
