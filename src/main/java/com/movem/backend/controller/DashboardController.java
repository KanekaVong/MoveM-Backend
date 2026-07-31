package com.movem.backend.controller;

import com.movem.backend.dto.response.Dashboard.DashboardResponse;
import com.movem.backend.service.DashboardServices.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/me")
    public DashboardResponse getMyDashboard() {

        return dashboardService.getMyDashboard();

    }
}
