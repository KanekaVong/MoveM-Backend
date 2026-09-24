package com.movem.backend.fitness.profileandgoal.services;

import com.movem.backend.fitness.profileandgoal.dtos.requests.CreateFitnessProfileRequest;
import com.movem.backend.fitness.profileandgoal.dtos.requests.UpdateFitnessProfileRequest;
import com.movem.backend.fitness.profileandgoal.dtos.responses.FitnessProfileResponse;

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