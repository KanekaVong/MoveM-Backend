package com.movem.backend.Service.Implement.FitnessServices.Club;

import com.movem.backend.Dto.request.FitnessRequest.Club.CreateFitnessClubRequest;
import com.movem.backend.Dto.request.FitnessRequest.Club.UpdateFitnessClubRequest;
import com.movem.backend.Dto.response.FitnessResponse.Club.FitnessClubResponse;
import com.movem.backend.Entity.Fitness.Club.FitnessClub;
import com.movem.backend.Entity.Fitness.Club.FitnessClubMember;
import com.movem.backend.Entity.Fitness.Club.FitnessClubMemberId;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.FitnessMapper.Club.FitnessClubMapper;
import com.movem.backend.Repository.FitnessRepository.Club.FitnessClubMemberRepository;
import com.movem.backend.Repository.FitnessRepository.Club.FitnessClubRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.FitnessServices.Club.FitnessClubService;
import com.movem.backend.model.enums.Fitness.ClubPrivacy;
import com.movem.backend.model.enums.Fitness.FitnessClubRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FitnessClubServiceImpl
        implements FitnessClubService {

    private final FitnessClubRepository fitnessClubRepository;

    private final FitnessClubMemberRepository fitnessClubMemberRepository;

    private final FitnessClubMapper fitnessClubMapper;

    private final CurrentUserService currentUserService;


    @Override
    public FitnessClubResponse createClub(
            CreateFitnessClubRequest request
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessClub club =
                new FitnessClub();

        club.setName(
                request.getName()
        );

        club.setDescription(
                request.getDescription()
        );

        club.setCreatedBy(
                currentUser
        );

        club.setPrivacy(
                request.getPrivacy()
        );

        club.setJoinToken(
                generateUniqueJoinToken()
        );

        LocalDateTime now =
                LocalDateTime.now();

        club.setCreatedAt(now);
        club.setUpdatedAt(now);

        FitnessClub saved =
                fitnessClubRepository.save(club);

        FitnessClubMemberId memberId =
                new FitnessClubMemberId();

        memberId.setClubId(saved.getId());
        memberId.setUserId(currentUser.getId());

        FitnessClubMember owner =
                new FitnessClubMember();

        owner.setId(memberId);
        owner.setFitnessClub(saved);
        owner.setUser(currentUser);
        owner.setRole(FitnessClubRole.OWNER);
        owner.setJoinedAt(LocalDateTime.now());

        fitnessClubMemberRepository.save(owner);

        return fitnessClubMapper.toResponse(saved);
    }


    @Override
    @Transactional
    public FitnessClubResponse getClub(
            Integer clubId
    ) {

        FitnessClub club =
                fitnessClubRepository
                        .findById(clubId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Fitness club not found."
                                )
                        );

        return fitnessClubMapper.toResponse(club);
    }


    @Override
    public FitnessClubResponse getClubByJoinToken(
            String joinToken
    ) {

        FitnessClub club =
                fitnessClubRepository
                        .findByJoinToken(joinToken)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Fitness club not found."
                                )
                        );

        return fitnessClubMapper.toResponse(club);
    }


    @Override
    @Transactional
    public List<FitnessClubResponse> getMyClubs() {

        User currentUser =
                currentUserService.getCurrentUser();

        return fitnessClubRepository
                .findByCreatedBy(currentUser)
                .stream()
                .map(fitnessClubMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional
    public List<FitnessClubResponse> getPublicClubs() {

        return fitnessClubRepository
                .findByPrivacy(ClubPrivacy.PUBLIC)
                .stream()
                .map(fitnessClubMapper::toResponse)
                .toList();
    }


    @Override
    public FitnessClubResponse updateClub(
            Integer clubId,
            UpdateFitnessClubRequest request
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessClub club =
                fitnessClubRepository
                        .findById(clubId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Fitness club not found."
                                )
                        );

        if (
                !club.getCreatedBy()
                        .getId()
                        .equals(currentUser.getId())
        ) {
            throw new IllegalArgumentException(
                    "You can only update a club that you created."
            );
        }

        club.setName(
                request.getName()
        );

        club.setDescription(
                request.getDescription()
        );

        club.setPrivacy(
                request.getPrivacy()
        );

        club.setUpdatedAt(
                LocalDateTime.now()
        );

        FitnessClub saved =
                fitnessClubRepository.save(club);

        return fitnessClubMapper.toResponse(saved);
    }


    @Override
    public void deleteClub(
            Integer clubId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessClub club =
                fitnessClubRepository
                        .findById(clubId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Fitness club not found."
                                )
                        );

        if (
                !club.getCreatedBy()
                        .getId()
                        .equals(currentUser.getId())
        ) {
            throw new IllegalArgumentException(
                    "You can only delete a club that you created."
            );
        }

        fitnessClubRepository.delete(club);
    }


    private String generateUniqueJoinToken() {

        String token;

        do {
            token = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 12)
                    .toUpperCase();

        } while (
                fitnessClubRepository.existsByJoinToken(token)
        );

        return token;
    }
}