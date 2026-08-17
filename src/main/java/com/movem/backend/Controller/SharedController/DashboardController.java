package com.movem.backend.Controller.SharedController;

import com.movem.backend.Dto.response.Dashboard.DashboardResponse;
import com.movem.backend.Service.DashboardServices.DashboardService;
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
