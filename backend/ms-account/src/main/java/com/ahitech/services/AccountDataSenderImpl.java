package com.ahitech.services;

import com.ahitech.dtos.AccountMessage;
import com.ahitech.dtos.UpdateAccountFullnameRequest;
import com.ahitech.exception.AppException;
import com.ahitech.factories.AccountMessageFactory;
import com.ahitech.services.interfaces.AccountDataSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountDataSenderImpl implements AccountDataSender {

    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;
    private final AccountMessageFactory factory;

    @Override
    public void sendMessage(String nickname, UpdateAccountFullnameRequest request) {
        if (nickname == null || request == null) {
            log.error("User data was lost");
            throw new AppException("Data was lost or nullable", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        AccountMessage message = factory.makeAccountMessage(nickname, request);

        try {
            String jsonRequestBody = objectMapper.writeValueAsString(message);
            rabbitTemplate.convertAndSend(
                    "account_exchange",
                    "account.data.update.routing.key",
                    jsonRequestBody
            );
            log.info("Message {} was successfully send in queue", jsonRequestBody);
        } catch (JsonProcessingException exception) {
            log.error("An error occurred while serializing and sending the request {}", request.toString());
            throw new AppException("An internal server error occurred, please try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
