package com.example.authservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    public static final String REGISTRATION_EVENT = "registration-event";
    public static final String REGISTRATION_EVENT_DLQ = "registration-event.dlq";

    @Bean
    Queue registrationEventQueue() {
        return QueueBuilder.durable(REGISTRATION_EVENT)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", REGISTRATION_EVENT_DLQ)
                .build();
    }

}