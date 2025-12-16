package com.bbu.hrms.notification_service.service;

import org.springframework.stereotype.Service;

@Service
public class ChannelSender {
    public void sendEmail(Long userId, String subject, String body) {
        // integrate sendgrid or smtp here, or call UserService to get email
        System.out.println("Sending EMAIL to user " + userId + ": " + subject);
    }

    public void sendPush(Long userId, String title, String body) {
        // integrate FCM or APNS
        System.out.println("Sending PUSH to user " + userId + ": " + title);
    }
}
