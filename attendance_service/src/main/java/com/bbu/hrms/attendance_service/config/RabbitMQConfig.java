package com.bbu.hrms.attendance_service.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.exchange.leave}")
    private String exchange;

    @Value("${rabbitmq.queue.leave.notification}")
    private String queueName;

    @Value("${rabbitmq.routing.leave.created}")
    private String createdRouting;


    @Bean
    public TopicExchange leaveExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Binding bindApproved() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(leaveExchange())
                .with(createdRouting);
    }

}

