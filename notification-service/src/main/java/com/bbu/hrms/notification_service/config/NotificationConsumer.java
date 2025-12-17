package com.bbu.hrms.notification_service.config;

import com.bbu.hrms.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import com.bbu.hrms.common.events.LeaveCreatedEvent;


@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queue.leave.notification}")
    public void onMessage(LeaveCreatedEvent message) {
        System.out.println("Received message: " + message);
        notificationService.processNotification(message);

    }
}
