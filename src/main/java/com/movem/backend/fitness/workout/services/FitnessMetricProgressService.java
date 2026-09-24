package com.movem.backend.fitness.workout.services;


import com.movem.backend.fitness.profileandgoal.dtos.responses.FitnessMetricProgressResponse;
import com.movem.backend.shared.analysis.statistics.dtos.responses.FitnessStatisticsResponse;

import java.util.List;

public interface FitnessMetricProgressService {

    List<FitnessMetricProgressResponse> getMetricProgress(
            FitnessStatisticsResponse statistics
    );

}