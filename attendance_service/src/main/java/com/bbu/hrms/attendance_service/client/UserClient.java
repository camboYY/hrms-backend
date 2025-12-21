package com.bbu.hrms.attendance_service.client;

import com.bbu.hrms.attendance_service.config.FeignClientConfig;
import com.bbu.hrms.attendance_service.dto.EmployeeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service",
        url = "${api.gateway.url}",
    configuration = FeignClientConfig.class)
public interface UserClient {

    /**
     * Calls User Service through API Gateway.
     * Pass the Authorization header so the gateway can forward it.
     */
    @GetMapping("/employees/{id}")
    EmployeeDTO getEmployeeByAuthUserId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") Long id // authUserId
    );
}
