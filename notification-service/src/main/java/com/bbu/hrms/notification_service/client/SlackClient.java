package com.bbu.hrms.notification_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "slackClient", url = "${slack.webhook.url}")
public interface SlackClient {

    @PostMapping(consumes = "application/json")
    void sendMessage(@RequestBody Map<String, String> payload);

}
