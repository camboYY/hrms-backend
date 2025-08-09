package com.bbu.hrms.notification_service.config;

// RabbitConfig.java

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String QUEUE = "leave-approval-notifications";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE, false);
    }
}
