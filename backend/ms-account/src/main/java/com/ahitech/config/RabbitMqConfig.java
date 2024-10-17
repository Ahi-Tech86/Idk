package com.ahitech.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue accountCreationQueue() {
        return new Queue("account_creation_queue", true);
    }

    @Bean
    public Queue accountFullnameUpdateQueue() {
        return new Queue("account_fullname_update_queue", true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("account_exchange");
    }

    @Bean
    public Binding bindingAccountCreation(Queue accountCreationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(accountCreationQueue)
                .to(exchange).with("account.routing.key");
    }

    @Bean
    public Binding bindingUserUpdate(Queue accountFullnameUpdateQueue, DirectExchange exchange) {
        return BindingBuilder.bind(accountFullnameUpdateQueue)
                .to(exchange).with("account.data.update.routing.key");
    }
}
