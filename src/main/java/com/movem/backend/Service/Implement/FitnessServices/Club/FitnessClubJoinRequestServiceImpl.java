package com.movem.backend.Service.Implement.FitnessServices.Club;

import com.movem.backend.Dto.response.FitnessResponse.Club.FitnessClubJoinRequestResponse;
import com.movem.backend.Entity.Fitness.Club.FitnessClub;
import com.movem.backend.Entity.Fitness.Club.FitnessClubJoinRequest;
import com.movem.backend.Entity.Fitness.Club.FitnessClubMember;
import com.movem.backend.Entity.Fitness.Club.FitnessClubMemberId;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.FitnessMapper.Club.FitnessClubJoinRequestMapper;
import com.movem.backend.Repository.FitnessRepository.Club.FitnessClubJoinRequestRepository;
import com.movem.backend.Repository.FitnessRepository.Club.FitnessClubMemberRepository;
import com.movem.backend.Repository.FitnessRepository.Club.FitnessClubRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.FitnessServices.Club.FitnessClubJoinRequestService;
import com.movem.backend.model.enums.Collaboration.JoinRequestStatus;
import com.movem.backend.model.enums.Fitness.FitnessClubRole;
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

    private final FitnessClubRepository fitnessClubRepository;

    private final FitnessClubJoinRequestRepository
            fitnessClubJoinRequestRepository;

    private final FitnessClubMemberRepository
            fitnessClubMemberRepository;

    private final FitnessClubJoinRequestMapper
            fitnessClubJoinRequestMapper;

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
                com.movem.backend.model.enums.Fitness.ClubPrivacy.PRIVATE) {

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


        /*
         * Prevent a second pending request.
         */

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

            /*
             * If the user somehow became a member already,
             * don't create a duplicate membership.
             */

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


    // =========================================================
    // REJECT REQUEST
    // OWNER / ADMIN ONLY
    // =========================================================

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

        return fitnessClubJoinRequestMapper.toResponse(
                saved
        );
    }


    // =========================================================
    // CANCEL MY REQUEST
    // =========================================================

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


    // =========================================================
    // GET MY REQUESTS
    // =========================================================

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


    // =========================================================
    // HELPERS
    // =========================================================

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