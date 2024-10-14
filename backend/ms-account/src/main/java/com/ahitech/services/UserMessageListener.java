package com.ahitech.services;

import com.ahitech.dtos.AccountCreationRequest;
import com.ahitech.exception.AppException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMessageListener {

    private final ObjectMapper objectMapper;
    private final AccountServiceImpl service;

    @RabbitListener(queues = "account_creation_queue")
    public void receiveMessage(String message) {
        try {
            AccountCreationRequest request = objectMapper.readValue(message, AccountCreationRequest.class);

            service.createAccount(request);
            log.info("Received and processed message for user email: {}", request.getEmail());
        } catch (JsonProcessingException exception) {
            log.error("Failed to parse incoming message: {}", message, exception);
            throw new AppException("Failed to process message", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
