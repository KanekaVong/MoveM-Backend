package com.movem.backend.Service.FitnessServices.ProfileAndGoal;


import com.movem.backend.Dto.response.FitnessResponse.ProfileAndGoal.FitnessMetricProgressResponse;
import com.movem.backend.Dto.response.StatisticsResponse.FitnessStatisticsResponse;

import java.util.List;

public interface FitnessMetricProgressService {

    List<FitnessMetricProgressResponse> getMetricProgress(
            FitnessStatisticsResponse statistics
    );

}