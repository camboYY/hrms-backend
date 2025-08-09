package com.bbu.hrms.leave_request.client;

import com.bbu.hrms.leave_request.config.FeignClientConfig;
import com.bbu.hrms.leave_request.dto.EmployeeInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "user-service",
        url = "http://localhost:8080",
        configuration = FeignClientConfig.class // 👈 API Gateway
)
public interface EmployeeClient {
    @GetMapping("employee/subordinates/{managerId}")
    List<EmployeeInfoDto> getSubordinates(@PathVariable("managerId") Long managerId);
}