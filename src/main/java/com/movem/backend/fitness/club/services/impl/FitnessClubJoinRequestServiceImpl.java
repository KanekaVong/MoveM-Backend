package com.movem.backend.fitness.club.services.impl;

import com.movem.backend.commons.enums.Fitness.ClubPrivacy;
import com.movem.backend.fitness.club.dtos.responses.FitnessClubJoinRequestResponse;
import com.movem.backend.fitness.club.entities.FitnessClub;
import com.movem.backend.fitness.club.entities.FitnessClubJoinRequest;
import com.movem.backend.fitness.club.entities.FitnessClubMember;
import com.movem.backend.fitness.club.entities.FitnessClubMemberId;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.fitness.club.mappers.FitnessClubJoinRequestMapper;
import com.movem.backend.fitness.club.repositories.FitnessClubJoinRequestRepository;
import com.movem.backend.fitness.club.repositories.FitnessClubMemberRepository;
import com.movem.backend.fitness.club.repositories.FitnessClubRepository;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.fitness.club.services.FitnessClubJoinRequestService;
import com.movem.backend.commons.Event.Factory.Fitness.FitnessClubEventFactory;
import com.movem.backend.shared.historyandlogs.featureevents.services.FeatureEventTrackingService;
import com.movem.backend.commons.enums.shared.JoinRequestStatus;
import com.movem.backend.commons.enums.Fitness.FitnessClubRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FitnessClubJoinRequestServiceImpl
        implements FitnessClubJoinRequestService {

    private final FeatureEventTrackingService featureEventTrackingService;
    private final FitnessClubEventFactory fitnessClubEventFactory;
    private final FitnessClubRepository fitnessClubRepository;
    private final FitnessClubJoinRequestRepository fitnessClubJoinRequestRepository;
    private final FitnessClubMemberRepository fitnessClubMemberRepository;
    private final FitnessClubJoinRequestMapper fitnessClubJoinRequestMapper;
    private final CurrentUserService currentUserService;

    @Override
    public FitnessClubJoinRequestResponse requestToJoin(
            Integer clubId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessClub club =
                getClub(clubId);


        if (club.getPrivacy() !=
                ClubPrivacy.PRIVATE) {

            throw new IllegalArgumentException(
                    "This club is public. You can join it directly."
            );
        }


        if (
                fitnessClubMemberRepository
                        .existsByFitnessClubAndUser(
                                club,
                                currentUser
                        )
        ) {

            throw new IllegalArgumentException(
                    "You are already a member of this club."
            );
        }

        boolean alreadyPending =
                fitnessClubJoinRequestRepository
                        .findByFitnessClubAndRequesterAndStatus(
                                club,
                                currentUser,
                                JoinRequestStatus.PENDING
                        )
                        .isPresent();

        if (alreadyPending) {

            throw new IllegalArgumentException(
                    "You already have a pending join request."
            );
        }


        FitnessClubJoinRequest request =
                new FitnessClubJoinRequest();

        request.setFitnessClub(club);
        request.setRequester(currentUser);
        request.setStatus(JoinRequestStatus.PENDING);
        request.setRequestedAt(LocalDateTime.now());

        FitnessClubJoinRequest saved =
                fitnessClubJoinRequestRepository.save(
                        request
                );

        featureEventTrackingService.handle(
                fitnessClubEventFactory.joinRequestSent(
                        club,
                        saved
                )
        );

        return fitnessClubJoinRequestMapper.toResponse(
                saved
        );
    }

    @Override
    @Transactional
    public List<FitnessClubJoinRequestResponse>
    getPendingRequests(
            Integer clubId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessClub club =
                getClub(clubId);

        FitnessClubMember membership =
                getMembership(
                        club,
                        currentUser
                );

        requireOwnerOrAdmin(membership);


        return fitnessClubJoinRequestRepository
                .findByFitnessClubAndStatus(
                        club,
                        JoinRequestStatus.PENDING
                )
                .stream()
                .map(
                        fitnessClubJoinRequestMapper::toResponse
                )
                .toList();
    }

    @Override
    public FitnessClubJoinRequestResponse approveRequest(
            Integer clubId,
            Long requestId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessClub club =
                getClub(clubId);

        FitnessClubMember manager =
                getMembership(
                        club,
                        currentUser
                );

        requireOwnerOrAdmin(manager);


        FitnessClubJoinRequest request =
                getRequest(
                        clubId,
                        requestId
                );


        if (
                request.getStatus()
                        != JoinRequestStatus.PENDING
        ) {

            throw new IllegalArgumentException(
                    "Only pending join requests can be approved."
            );
        }


        User requester =
                request.getRequester();


        if (
                fitnessClubMemberRepository
                        .existsByFitnessClubAndUser(
                                club,
                                requester
                        )
        ) {

            request.setStatus(
                    JoinRequestStatus.APPROVED
            );

            request.setRespondedAt(
                    LocalDateTime.now()
            );

            FitnessClubJoinRequest saved =
                    fitnessClubJoinRequestRepository.save(
                            request);

            featureEventTrackingService.handle(
                    fitnessClubEventFactory.joinRequestApproved(
                            club,
                            saved,
                            currentUser
                    )
            );

            return fitnessClubJoinRequestMapper
                    .toResponse(saved);
        }


        FitnessClubMemberId memberId =
                new FitnessClubMemberId();

        memberId.setClubId(
                club.getId()
        );

        memberId.setUserId(
                requester.getId()
        );


        FitnessClubMember member =
                new FitnessClubMember();

        member.setId(memberId);
        member.setFitnessClub(club);
        member.setUser(requester);
        member.setRole(FitnessClubRole.MEMBER);
        member.setJoinedAt(LocalDateTime.now());

        fitnessClubMemberRepository.save(
                member
        );


        request.setStatus(
                JoinRequestStatus.APPROVED
        );

        request.setRespondedAt(
                LocalDateTime.now()
        );


        FitnessClubJoinRequest saved =
                fitnessClubJoinRequestRepository.save(
                        request
                );

        return fitnessClubJoinRequestMapper.toResponse(
                saved
        );
    }

    @Override
    public FitnessClubJoinRequestResponse rejectRequest(
            Integer clubId,
            Long requestId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessClub club =
                getClub(clubId);

        FitnessClubMember manager =
                getMembership(
                        club,
                        currentUser
                );

        requireOwnerOrAdmin(manager);


        FitnessClubJoinRequest request =
                getRequest(
                        clubId,
                        requestId
                );


        if (
                request.getStatus()
                        != JoinRequestStatus.PENDING
        ) {

            throw new IllegalArgumentException(
                    "Only pending join requests can be rejected."
            );
        }


        request.setStatus(
                JoinRequestStatus.REJECTED
        );

        request.setRespondedAt(
                LocalDateTime.now()
        );


        FitnessClubJoinRequest saved =
                fitnessClubJoinRequestRepository.save(
                        request
                );

        featureEventTrackingService.handle(
                fitnessClubEventFactory.joinRequestRejected(
                        club,
                        saved,
                        currentUser
                )
        );

        return fitnessClubJoinRequestMapper.toResponse(
                saved
        );
    }

    @Override
    public void cancelRequest(
            Long requestId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessClubJoinRequest request =
                fitnessClubJoinRequestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Join request not found."
                                )
                        );


        if (
                !request.getRequester()
                        .getId()
                        .equals(currentUser.getId())
        ) {

            throw new IllegalArgumentException(
                    "You can only cancel your own join request."
            );
        }


        if (
                request.getStatus()
                        != JoinRequestStatus.PENDING
        ) {

            throw new IllegalArgumentException(
                    "Only pending join requests can be cancelled."
            );
        }


        request.setStatus(
                JoinRequestStatus.REJECTED
        );

        request.setRespondedAt(
                LocalDateTime.now()
        );

        fitnessClubJoinRequestRepository.save(
                request
        );
    }

    @Override
    @Transactional
    public List<FitnessClubJoinRequestResponse>
    getMyRequests() {

        User currentUser =
                currentUserService.getCurrentUser();

        return fitnessClubJoinRequestRepository
                .findByRequester(currentUser)
                .stream()
                .map(
                        fitnessClubJoinRequestMapper::toResponse
                )
                .toList();
    }

    private FitnessClub getClub(
            Integer clubId
    ) {

        return fitnessClubRepository
                .findById(clubId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Fitness club not found."
                        )
                );
    }


    private FitnessClubJoinRequest getRequest(
            Integer clubId,
            Long requestId
    ) {

        FitnessClubJoinRequest request =
                fitnessClubJoinRequestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Join request not found."
                                )
                        );


        if (
                request.getFitnessClub() == null ||
                        !request.getFitnessClub()
                                .getId()
                                .equals(clubId)
        ) {

            throw new ResourceNotFoundException(
                    "Join request does not belong to this club."
            );
        }


        return request;
    }


    private FitnessClubMember getMembership(
            FitnessClub club,
            User user
    ) {

        return fitnessClubMemberRepository
                .findByFitnessClubAndUser(
                        club,
                        user
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "You are not a member of this club."
                        )
                );
    }


    private void requireOwnerOrAdmin(
            FitnessClubMember member
    ) {

        if (
                member.getRole()
                        != FitnessClubRole.OWNER
                        &&
                        member.getRole()
                                != FitnessClubRole.ADMIN
        ) {

            throw new IllegalArgumentException(
                    "You do not have permission to manage join requests."
            );
        }
    }
}