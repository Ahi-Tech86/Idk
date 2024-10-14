package com.ahitech.services;

import com.ahitech.dtos.UserDto;
import com.ahitech.dtos.UserMessageDto;
import com.ahitech.exception.AppException;
import com.ahitech.factories.UserMessageDtoFactory;
import com.ahitech.services.interfaces.UserAccountMessagingService;
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
public class UserAccountMessagingServiceImpl implements UserAccountMessagingService {

    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;
    private final UserMessageDtoFactory factory;

    @Override
    public void sendUserData(UserDto userDto, Long userId) {
        if (userDto == null || userId == null) {
            log.error("User data was lost");
            throw new AppException("Data was lost or nullable", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        UserMessageDto messageDto = factory.makeUserMessageDto(userDto, userId);

        try {
            String jsonRequestBody = objectMapper.writeValueAsString(messageDto);
            rabbitTemplate.convertAndSend("account_exchange", "account.routing.key", jsonRequestBody);
            log.info("Message {} was successfully send in queue", jsonRequestBody);
        } catch (JsonProcessingException exception) {
            log.error("An error occurred while serializing and sending the request {}", userDto.toString());
            throw new AppException("An internal server error occurred, please try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
