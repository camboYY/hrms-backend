package com.bbu.hrms.notification_service.config;

import com.bbu.hrms.notification_service.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;


    @Value("${rabbitmq.exchange.leave}")
    private String exchange;

    @Value("${rabbitmq.routing.leave.created}")
    private String createdRouting;

    @Value("${rabbitmq.routing.leave.approved}")
    private String approvedRouting;

    @Value("${rabbitmq.routing.leave.rejected}")
    private String rejectedRouting;


    public void sendNotification(NotificationMessage message) {
        rabbitTemplate.convertAndSend(exchange, rejectedRouting, message);
        System.out.println("Published to RabbitMQ: " + message);
    }

}

