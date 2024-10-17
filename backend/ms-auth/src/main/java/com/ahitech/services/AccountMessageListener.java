package com.ahitech.services;

import com.ahitech.dtos.FullnameUpdateRequest;
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
public class AccountMessageListener {

    private final UserServiceImpl service;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "account_fullname_update_queue")
    public void receiveMessage(String message) {
        try {
            FullnameUpdateRequest request = objectMapper.readValue(message, FullnameUpdateRequest.class);

            service.updateFullname(request);
            log.info("Received and processed message for user email: {}", request.getNickname());
        } catch (JsonProcessingException exception) {
            log.error("Failed to parse incoming message: {}", message, exception);
            throw new AppException("Failed to process message", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
