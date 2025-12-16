package com.bbu.hrms.leave_request.service;

import com.bbu.hrms.common.events.LeaveApprovedEvent;
import com.bbu.hrms.common.events.LeaveCreatedEvent;
import com.bbu.hrms.common.events.LeaveRejectedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LeaveEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.leave}")
    private String exchange;

    @Value("${rabbitmq.routing.leave.created}")
    private String createdRouting;

    @Value("${rabbitmq.routing.leave.approved}")
    private String approvedRouting;

    @Value("${rabbitmq.routing.leave.rejected}")
    private String rejectedRouting;


    public LeaveEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishLeaveCreatedEvent(LeaveCreatedEvent event) {
        rabbitTemplate.convertAndSend(exchange, createdRouting, event);
    }

    public void publishLeaveApprovedEvent(LeaveApprovedEvent event) {
        rabbitTemplate.convertAndSend(exchange, approvedRouting, event);
    }

    public void publishLeaveRejectedEvent(LeaveRejectedEvent event) {
        rabbitTemplate.convertAndSend(exchange, rejectedRouting, event);
    }
}