package com.ahitech.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue queue() {
        return new Queue("account_creation_queue", true);
    }

    @Bean
    public Queue queueUserUpdate() {
        return new Queue("account_fullname_update_queue", true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("account_exchange");
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("account.routing.key");
    }

    @Bean
    public Binding bindingUserUpdate(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("account.data.update.routing.key");
    }
}
