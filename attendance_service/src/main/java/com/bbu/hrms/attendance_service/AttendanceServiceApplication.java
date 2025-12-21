package com.bbu.hrms.attendance_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {
		"com.bbu.hrms.attendance_service",
		"com.bbu.hrms.common"   // ✅ add this
})@EnableFeignClients(basePackages = "com.bbu.hrms.attendance_service.client")
public class AttendanceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendanceServiceApplication.class, args);
	}

}
