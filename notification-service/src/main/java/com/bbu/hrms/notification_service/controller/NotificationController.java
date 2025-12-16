package com.bbu.hrms.notification_service.controller;

import com.bbu.hrms.notification_service.config.NotificationPublisher;
import com.bbu.hrms.notification_service.dto.NotificationMessage;
import com.bbu.hrms.notification_service.entity.Notification;
import com.bbu.hrms.notification_service.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Controller", description = "API endpoints for notifications")
public class NotificationController {

    private final NotificationPublisher publisher;
    private final NotificationService service;

    @Operation(summary = "Publish a notification")
    @PostMapping("/publish")
    public String publish(@RequestBody NotificationMessage message) {
        publisher.sendNotification(message);
        return "Notification sent!";
    }

    @Operation(summary = "Get all notifications for a user")
    @GetMapping("/user/{userId}")
    public List<Notification> getByUser(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }
}
