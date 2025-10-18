package com.bbu.hrms.attendance_service.listener;


import com.bbu.hrms.attendance_service.config.RabbitMQConfig;
import com.bbu.hrms.attendance_service.entity.Attendance;
import com.bbu.hrms.attendance_service.repository.AttendanceRepository;
import com.bbu.hrms.common.events.LeaveApprovedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AttendanceEventListener {

    private final AttendanceRepository attendanceRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ATTENDANCE_LEAVE_APPROVED)
    public void handleLeaveApproved(LeaveApprovedEvent event) {
        System.out.println("📩 Received leave approved event: " + event);

        LocalDate date = event.getStartDate();
        while (!date.isAfter(event.getEndDate())) {
            Attendance record = Attendance.builder()
                    .employeeId(event.getEmployeeId())
                    .date(date)
                    .status("LEAVE")
                    .note("Leave approved: " + event.getLeaveType())
                    .build();

            attendanceRepository.save(record);
            date = date.plusDays(1);
        }
    }
}
