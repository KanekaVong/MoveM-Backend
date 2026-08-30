package com.movem.backend.Service.FitnessServices.Club;

import com.movem.backend.Dto.request.FitnessRequest.Club.CreateFitnessClubRequest;
import com.movem.backend.Dto.request.FitnessRequest.Club.UpdateFitnessClubRequest;
import com.movem.backend.Dto.response.FitnessResponse.Club.FitnessClubResponse;
import jakarta.transaction.Transactional;

import java.util.List;

public interface FitnessClubService {

    FitnessClubResponse createClub(
            CreateFitnessClubRequest request
    );

    FitnessClubResponse getClub(
            Integer clubId
    );

    FitnessClubResponse getClubByJoinToken(
            String joinToken
    );

    List<FitnessClubResponse> getMyClubs();

    List<FitnessClubResponse> getPublicClubs();

    @Transactional
    List<FitnessClubResponse> searchClubs(
            String keyword
    );

    FitnessClubResponse updateClub(
            Integer clubId,
            UpdateFitnessClubRequest request
    );

    void deleteClub(
            Integer clubId
    );

}
