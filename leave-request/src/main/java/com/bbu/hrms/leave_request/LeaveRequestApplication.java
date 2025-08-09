package com.bbu.hrms.leave_request;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.bbu.hrms.leave_request.client")
public class LeaveRequestApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeaveRequestApplication.class, args);
	}

}
