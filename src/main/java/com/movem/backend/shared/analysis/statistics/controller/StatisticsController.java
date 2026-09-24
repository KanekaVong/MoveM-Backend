package com.movem.backend.shared.analysis.statistics.controller;

import com.movem.backend.shared.analysis.statistics.dtos.responses.FitnessStatisticsResponse;
import com.movem.backend.shared.analysis.statistics.dtos.responses.TaskStatisticsResponse;
import com.movem.backend.shared.analysis.statistics.services.FitnessStatisticsService;
import com.movem.backend.shared.analysis.statistics.services.TaskStatisticsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
@Tag(
        name = "Social - Statistics"
)
@RequiredArgsConstructor
public class StatisticsController {

    private final TaskStatisticsService taskStatisticsService;
    private final FitnessStatisticsService fitnessStatisticsService;

    @GetMapping("/tasks")
    public TaskStatisticsResponse getTaskStatistics() {

        return taskStatisticsService.getMyTaskStatistics();

    }

    @GetMapping("/fitness")
    public FitnessStatisticsResponse getFitnessStatistics() {

        return fitnessStatisticsService
                .getMyFitnessStatistics();
    }

}