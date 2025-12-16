package com.bbu.hrms.notification_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String title;

    @Column(name = "template_code", nullable = false)
    private String templateCode;

    private String body;

    private String type; // EMAIL | SMS | IN-APP | SLACK | PUSH

    private String status; // PENDING, SENT, FAILED

    @Column(name = "sent_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime sentAt;

    private LocalDateTime createdAt = LocalDateTime.now();
}
