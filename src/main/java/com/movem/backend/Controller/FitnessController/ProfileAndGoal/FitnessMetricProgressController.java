package com.movem.backend.Controller.FitnessController.ProfileAndGoal;

import com.movem.backend.Dto.response.FitnessResponse.ProfileAndGoal.FitnessMetricProgressResponse;
import com.movem.backend.Service.FitnessServices.ProfileAndGoal.FitnessMetricProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fitness/metrics")
@RequiredArgsConstructor
public class FitnessMetricProgressController {

}