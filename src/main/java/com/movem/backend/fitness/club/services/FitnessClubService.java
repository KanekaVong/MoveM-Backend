package com.movem.backend.fitness.club.services;

import com.movem.backend.fitness.club.dtos.requests.CreateFitnessClubRequest;
import com.movem.backend.fitness.club.dtos.requests.UpdateFitnessClubRequest;
import com.movem.backend.fitness.club.dtos.responses.FitnessClubResponse;
import jakarta.transaction.Transactional;

import java.util.List;

public interface FitnessClubService {

    FitnessClubResponse createClub(CreateFitnessClubRequest request);
    FitnessClubResponse getClub(Integer clubId);
    FitnessClubResponse getClubByJoinToken(String joinToken);

    List<FitnessClubResponse> getMyClubs();
    List<FitnessClubResponse> getPublicClubs();

    @Transactional
    List<FitnessClubResponse> searchClubs(String keyword);

    FitnessClubResponse updateClub(Integer clubId, UpdateFitnessClubRequest request);

    void deleteClub(Integer clubId);

}
