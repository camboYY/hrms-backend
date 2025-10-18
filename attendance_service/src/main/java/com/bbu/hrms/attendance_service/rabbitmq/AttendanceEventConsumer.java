package com.bbu.hrms.attendance_service.rabbitmq;

import com.bbu.hrms.common.events.LeaveApprovedEvent;
import com.bbu.hrms.attendance_service.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttendanceEventConsumer {

    private final AttendanceService attendanceService;

    @RabbitListener(queues = "${rabbitmq.queue.leave.approved}")
    public void handleLeaveApproved(LeaveApprovedEvent event) {
        System.out.println("🎯 Received LeaveApprovedEvent for Employee ID: " + event.getEmployeeId());
        attendanceService.markAttendance(event.getEmployeeId(), "ON_LEAVE");
    }
}
