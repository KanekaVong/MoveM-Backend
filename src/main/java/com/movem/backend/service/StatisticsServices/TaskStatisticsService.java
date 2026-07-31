package com.movem.backend.service.StatisticsServices;

import com.movem.backend.dto.response.StatisticsResponse.TaskStatisticsResponse;

public interface TaskStatisticsService {

    TaskStatisticsResponse getMyTaskStatistics();

}