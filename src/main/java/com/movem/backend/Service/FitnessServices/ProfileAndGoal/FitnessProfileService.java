package com.movem.backend.Service.FitnessServices.ProfileAndGoal;

import com.movem.backend.Dto.request.FitnessRequest.ProfileAndGoal.CreateFitnessProfileRequest;
import com.movem.backend.Dto.request.FitnessRequest.ProfileAndGoal.UpdateFitnessProfileRequest;
import com.movem.backend.Dto.response.FitnessResponse.ProfileAndGoal.FitnessProfileResponse;

public interface FitnessProfileService {

    FitnessProfileResponse createProfile(
            CreateFitnessProfileRequest request
    );

    FitnessProfileResponse getMyProfile();

    FitnessProfileResponse updateProfile(
            UpdateFitnessProfileRequest request
    );

    void deleteProfile();
}