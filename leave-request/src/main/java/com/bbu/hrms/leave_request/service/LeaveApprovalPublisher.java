package com.bbu.hrms.leave_request.service;

import com.bbu.hrms.leave_request.config.RabbitConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaveApprovalPublisher {

    private final AmqpTemplate amqpTemplate;

    @Autowired
    public LeaveApprovalPublisher(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendLeaveApprovalNotification(String message) {
        amqpTemplate.convertAndSend(RabbitConfig.QUEUE, message);
    }
}