package com.ahitech.services;

public interface EmailService {
    void sendActivationCodeToEmail(String to, String activationCode);
}
