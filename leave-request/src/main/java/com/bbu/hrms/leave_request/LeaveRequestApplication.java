package com.bbu.hrms.leave_request;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.bbu.hrms.leave_request.client")
@EnableScheduling
public class LeaveRequestApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeaveRequestApplication.class, args);
	}

}
