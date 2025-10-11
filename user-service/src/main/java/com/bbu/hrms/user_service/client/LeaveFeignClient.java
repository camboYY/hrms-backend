package com.bbu.hrms.user_service.client;

import com.bbu.hrms.user_service.config.FeignClientConfig;
import com.bbu.hrms.user_service.dto.LeaveRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient(
        name = "leave-service",
        url = "http://localhost:8080",
        configuration = FeignClientConfig.class // 👈 API Gateway
)
public interface LeaveFeignClient {
    // Feign will send employeeIds as CSV by default: ?employeeIds=1,2,3
    @GetMapping("/leaves")
    List<LeaveRequestDTO> getLeavesByEmployeeIdsAndStatus(
            @RequestParam("employeeIds") List<Long> employeeIds,
            @RequestParam(value = "status", required = false) String status
    );
}
