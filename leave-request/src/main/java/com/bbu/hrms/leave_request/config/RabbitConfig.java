package com.bbu.hrms.leave_request.config;

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


