package com.movem.backend.shared.analysis.statistics.services;

import com.movem.backend.shared.analysis.statistics.dtos.responses.TaskStatisticsResponse;

public interface TaskStatisticsService {

    TaskStatisticsResponse getMyTaskStatistics();

}