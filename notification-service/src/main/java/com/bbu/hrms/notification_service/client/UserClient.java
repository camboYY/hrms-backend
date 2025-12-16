package com.bbu.hrms.notification_service.client;

import com.bbu.hrms.notification_service.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8080", configuration = FeignClientConfig.class)
public interface UserClient {

    @GetMapping("/employees/{id}")
    Object getUserById(@PathVariable Long id);
}
