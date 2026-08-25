package com.movem.backend.Controller.SharedController;

import com.movem.backend.Dto.response.StatisticsResponse.FitnessStatisticsResponse;
import com.movem.backend.Dto.response.StatisticsResponse.TaskStatisticsResponse;
import com.movem.backend.Service.StatisticsServices.FitnessStatisticsService;
import com.movem.backend.Service.StatisticsServices.TaskStatisticsService;
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