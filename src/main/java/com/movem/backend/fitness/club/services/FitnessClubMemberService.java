package com.movem.backend.fitness.club.services;

import com.movem.backend.fitness.club.dtos.requests.AddFitnessClubMemberRequest;
import com.movem.backend.fitness.club.dtos.requests.UpdateFitnessClubMemberRoleRequest;
import com.movem.backend.fitness.club.dtos.responses.FitnessClubMemberResponse;

import java.util.List;

public interface FitnessClubMemberService {

    FitnessClubMemberResponse addMember(Integer clubId, AddFitnessClubMemberRequest request);
    FitnessClubMemberResponse addCurrentUserAsMember(Integer clubId);

    List<FitnessClubMemberResponse> getClubMembers(Integer clubId);

    FitnessClubMemberResponse getMember(Integer clubId, Integer userId);
    FitnessClubMemberResponse updateMemberRole(Integer clubId, Integer userId, UpdateFitnessClubMemberRoleRequest request);

    void removeMember(Integer clubId, Integer userId);
}