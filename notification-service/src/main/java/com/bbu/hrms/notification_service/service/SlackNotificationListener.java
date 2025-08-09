package com.bbu.hrms.notification_service.service;


import com.bbu.hrms.notification_service.client.SlackClient;
import com.bbu.hrms.notification_service.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SlackNotificationListener {

    private final SlackClient slackClient;

    public SlackNotificationListener(SlackClient slackClient) {
        this.slackClient = slackClient;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void receiveMessage(String message) {
        slackClient.sendMessage(Map.of("text", message));
    }
}
