package com.bbu.hrms.attendance_service.listener;


import com.bbu.hrms.attendance_service.service.AttendanceService;
import com.bbu.hrms.common.events.LeaveApprovedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AttendanceEventListener {

    private final AttendanceService attendanceService;

    @RabbitListener(queues = {"${rabbitmq.queue.leave.notification}"})
    public void onMessage(LeaveApprovedEvent event) {
        System.out.println("📩 Received leave approved event: " + event);

        // LeaveEvent contains: employeeId, leaveDate, status
        LocalDate date = event.getStartDate();
        while (!date.isAfter(event.getEndDate())) {
            attendanceService.markAttendanceByLeaveEvent(
                    event.getEmployeeId(),
                    date,
                    event.getStatus()
            );
            date = date.plusDays(1);
        }
    }
}
