package com.movem.backend.shared.analysis.statistics.services;

import com.movem.backend.shared.analysis.statistics.dtos.responses.FitnessStatisticsResponse;

public interface FitnessStatisticsService {

    FitnessStatisticsResponse getMyFitnessStatistics();
}