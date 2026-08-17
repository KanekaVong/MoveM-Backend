package com.movem.backend.Controller.SharedController;

import com.movem.backend.Dto.response.StatisticsResponse.TaskStatisticsResponse;
import com.movem.backend.Service.StatisticsServices.TaskStatisticsService;
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