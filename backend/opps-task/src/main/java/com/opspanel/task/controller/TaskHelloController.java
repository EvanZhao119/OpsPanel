package com.opspanel.task.controller;

import com.opspanel.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskHelloController {
    @GetMapping("/api/task/_hello")
    public ApiResponse<String> hello() {
        return ApiResponse.ok("task module ok");
    }
}
