package com.bbu.hrms.notification_service.service;

import com.bbu.hrms.notification_service.entity.Notification;
import com.bbu.hrms.notification_service.entity.NotificationTemplate;
import com.bbu.hrms.notification_service.repository.NotificationRepository;
import com.bbu.hrms.notification_service.repository.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bbu.hrms.common.events.LeaveCreatedEvent;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository templateRepository;
    private final SlackService slackService;
    private final ChannelSender channelSender; // email/push placeholders

    @Transactional
    public void processNotification(LeaveCreatedEvent message) {

        String templateCode = switch (message.getStatus()) {
            case "APPROVED" -> "LEAVE_APPROVED";
            case "REJECTED"  -> "LEAVE_REJECTED";
            default -> "LEAVE_CREATED";
        };

        NotificationTemplate template = templateRepository.findByCode(templateCode)
                .orElse(null);

        String title = template != null ? template.getTitle() : "Leave Update";
        String body = template != null ? template.getBody() : "Your leave status: " + message.getStatus();

        // Persist notification (initially PENDING)
        Notification notif = Notification.builder()
                .userId(message.getEmployeeId())
                .templateCode(templateCode)
                .type(template != null ? template.getChannel() : "in_app")
                .title(title)
                .body(body)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        notif = notificationRepository.save(notif);

        // Send via channel(s). For demo: if channel == "push" or "in_app" we just mark SENT,
        // if channel == "slack" or we always send to Slack too (configurable)
        boolean allSent = true;

        try {
            // Send Slack notification (always send a Slack fallback)
            // Push to Slack
            String msg = "📢 *Leave Request Update*\n" +
                    "User ID: " + message.getEmployeeId() + "\n" +
                    "Leave Type: " + message.getLeaveType() + "\n" +
                    "Start Date: " + message.getStartDate() + "\n" +
                    "End Date: " + message.getEndDate() + "\n" +
                    "Reason: " + message.getMessage() + "\n" +
                    "Status: *" + message.getStatus() + "*";
            slackService.sendSlackMessage(msg);

            // Send via specific channel if necessary
            if (notif.getType() != null) {
                switch (notif.getType()) {
                    case "email" -> channelSender.sendEmail(notif.getUserId(), notif.getTitle(), notif.getBody());
                    case "push"  -> channelSender.sendPush(notif.getUserId(), notif.getTitle(), notif.getBody());
                    case "in_app" -> { /* internal, stored in DB already */ }
                    default -> {}
                }
            }
        } catch (Exception ex) {
            allSent = false;
            System.err.println("Failed to send  message: " + ex.getMessage());
        }

        notif.setStatus(allSent ? "SENT" : "FAILED");
        notif.setSentAt(LocalDateTime.now());
        notif.setType(message.getLeaveType());

        notificationRepository.save(notif);
    }

    public List<Notification> findByUserId(Long id) {
        return notificationRepository.findByUserId(id).stream().toList();
    }
}

