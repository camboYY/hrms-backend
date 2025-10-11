package com.bbu.hrms.leave_request.config;

import com.bbu.hrms.leave_request.dto.LeaveApprovedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LeaveEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.leave}")
    private String exchange;

    @Value("${rabbitmq.routing.leave.approved}")
    private String routingKey;

    public LeaveEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishLeaveApprovedEvent(LeaveApprovedEvent event) {
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}
