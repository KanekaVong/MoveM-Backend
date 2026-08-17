package com.movem.backend.Dto.response.FitnessResponse.ProfileAndGoal;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class FitnessProfileResponse {

    private Integer userId;

    private BigDecimal height;

    private BigDecimal weight;

    private BigDecimal bmi;

    private LocalDateTime updatedAt;
}