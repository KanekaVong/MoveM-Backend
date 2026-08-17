package com.movem.backend.Service.FitnessServices.Club;

import com.movem.backend.Dto.request.FitnessRequest.Club.AddFitnessClubMemberRequest;
import com.movem.backend.Dto.request.FitnessRequest.Club.UpdateFitnessClubMemberRoleRequest;
import com.movem.backend.Dto.response.FitnessResponse.Club.FitnessClubMemberResponse;

import java.util.List;

public interface FitnessClubMemberService {

    FitnessClubMemberResponse addMember(
            Integer clubId,
            AddFitnessClubMemberRequest request
    );

    FitnessClubMemberResponse addCurrentUserAsMember(
            Integer clubId
    );

    List<FitnessClubMemberResponse> getClubMembers(
            Integer clubId
    );

    FitnessClubMemberResponse getMember(
            Integer clubId,
            Integer userId
    );

    FitnessClubMemberResponse updateMemberRole(
            Integer clubId,
            Integer userId,
            UpdateFitnessClubMemberRoleRequest request
    );

    void removeMember(
            Integer clubId,
            Integer userId
    );
}