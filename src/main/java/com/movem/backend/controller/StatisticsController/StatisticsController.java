package com.movem.backend.controller.StatisticsController;

import com.movem.backend.dto.response.StatisticsResponse.TaskStatisticsResponse;
import com.movem.backend.service.StatisticsServices.TaskStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final TaskStatisticsService taskStatisticsService;

    @GetMapping("/tasks")
    public TaskStatisticsResponse getTaskStatistics() {

        return taskStatisticsService.getMyTaskStatistics();

    }

}