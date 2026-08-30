package com.movem.backend.Service.Implement.FitnessServices.Club;

import com.movem.backend.Dto.request.FitnessRequest.Club.AddFitnessClubMemberRequest;
import com.movem.backend.Dto.request.FitnessRequest.Club.UpdateFitnessClubMemberRoleRequest;
import com.movem.backend.Dto.response.FitnessResponse.Club.FitnessClubMemberResponse;
import com.movem.backend.Entity.Fitness.Club.FitnessClub;
import com.movem.backend.Entity.Fitness.Club.FitnessClubMember;
import com.movem.backend.Entity.Fitness.Club.FitnessClubMemberId;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.FitnessMapper.Club.FitnessClubMemberMapper;
import com.movem.backend.Repository.FitnessRepository.Club.FitnessClubMemberRepository;
import com.movem.backend.Repository.FitnessRepository.Club.FitnessClubRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.FitnessServices.Club.FitnessClubMemberService;
import com.movem.backend.Service.Event.Factory.Fitness.FitnessClubEventFactory;
import com.movem.backend.Service.Event.FeatureEventTrackingService;
import com.movem.backend.model.enums.Fitness.FitnessClubRole;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FitnessClubMemberServiceImpl implements FitnessClubMemberService {

    private final FeatureEventTrackingService featureEventTrackingService;
    private final FitnessClubEventFactory fitnessClubEventFactory;
    private final FitnessClubMemberRepository fitnessClubMemberRepository;
    private final FitnessClubRepository fitnessClubRepository;
    private final FitnessClubMemberMapper fitnessClubMemberMapper;
    private final CurrentUserService currentUserService;
    private final EntityManager entityManager;

    @Override
    public FitnessClubMemberResponse addMember(
            Integer clubId,
            AddFitnessClubMemberRequest request
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


        User userToAdd =
                entityManager.find(
                        User.class,
                        request.getUserId()
                );

        if (userToAdd == null) {
            throw new ResourceNotFoundException(
                    "User not found."
            );
        }


        if (
                fitnessClubMemberRepository
                        .existsByFitnessClubAndUser(
                                club,
                                userToAdd
                        )
        ) {

            throw new IllegalArgumentException(
                    "User is already a member of this club."
            );
        }


        FitnessClubMember member =
                createMember(
                        club,
                        userToAdd,
                        FitnessClubRole.MEMBER
                );

        FitnessClubMember saved =
                fitnessClubMemberRepository.save(
                        member
                );

        featureEventTrackingService.handle(
                fitnessClubEventFactory.memberAdded(
                        club,
                        currentUser,
                        saved
                )
        );

        return fitnessClubMemberMapper.toResponse(
                saved
        );
    }

    @Override
    public FitnessClubMemberResponse
    addCurrentUserAsMember(
            Integer clubId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessClub club =
                getClub(clubId);


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


        FitnessClubMember member =
                createMember(
                        club,
                        currentUser,
                        FitnessClubRole.MEMBER
                );

        FitnessClubMember saved =
                fitnessClubMemberRepository.save(
                        member
                );

        return fitnessClubMemberMapper.toResponse(
                saved
        );
    }

    @Override
    @Transactional
    public List<FitnessClubMemberResponse> getClubMembers(
            Integer clubId
    ) {

        FitnessClub club =
                getClub(clubId);

        return fitnessClubMemberRepository
                .findByFitnessClub(club)
                .stream()
                .map(
                        fitnessClubMemberMapper::toResponse
                )
                .toList();
    }

    @Override
    @Transactional
    public FitnessClubMemberResponse getMember(
            Integer clubId,
            Integer userId
    ) {

        FitnessClub club =
                getClub(clubId);

        User user =
                entityManager.find(
                        User.class,
                        userId
                );

        if (user == null) {
            throw new ResourceNotFoundException(
                    "User not found."
            );
        }


        FitnessClubMember member =
                fitnessClubMemberRepository
                        .findByFitnessClubAndUser(
                                club,
                                user
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User is not a member of this club."
                                )
                        );

        return fitnessClubMemberMapper.toResponse(
                member
        );
    }

    @Override
    public FitnessClubMemberResponse updateMemberRole(
            Integer clubId,
            Integer userId,
            UpdateFitnessClubMemberRoleRequest request
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


        FitnessClubMember member =
                findMember(
                        club,
                        userId
                );

        if (
                request.getRole()
                        == FitnessClubRole.OWNER
        ) {

            if (
                    manager.getRole()
                            != FitnessClubRole.OWNER
            ) {

                throw new IllegalArgumentException(
                        "Only the club owner can assign the OWNER role."
                );
            }

            throw new IllegalArgumentException(
                    "Use an ownership-transfer operation to transfer ownership."
            );
        }

        if (
                member.getRole()
                        == FitnessClubRole.ADMIN
                        &&
                        manager.getRole()
                                != FitnessClubRole.OWNER
        ) {

            throw new IllegalArgumentException(
                    "Only the club owner can change an admin's role."
            );
        }

        if (
                member.getRole()
                        == FitnessClubRole.OWNER
        ) {

            throw new IllegalArgumentException(
                    "Club ownership cannot be changed here."
            );
        }


        String oldRole = member.getRole().name();
        member.setRole(request.getRole());



        FitnessClubMember saved =
                fitnessClubMemberRepository.save(
                        member
                );

        featureEventTrackingService.handle(
                fitnessClubEventFactory.memberRoleUpdated(
                        club,
                        currentUser,
                        saved,
                        oldRole
                )
        );

        return fitnessClubMemberMapper.toResponse(
                saved
        );
    }

    @Override
    public void removeMember(
            Integer clubId,
            Integer userId
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


        FitnessClubMember member =
                findMember(
                        club,
                        userId
                );


        if (
                member.getRole()
                        == FitnessClubRole.OWNER
        ) {

            throw new IllegalArgumentException(
                    "The club owner cannot be removed."
            );
        }

        if (
                member.getRole()
                        == FitnessClubRole.ADMIN
                        &&
                        manager.getRole()
                                != FitnessClubRole.OWNER
        ) {

            throw new IllegalArgumentException(
                    "Only the club owner can remove an admin."
            );
        }


        fitnessClubMemberRepository.delete(
                member
        );

        featureEventTrackingService.handle(
                fitnessClubEventFactory.memberRemoved(
                        club,
                        currentUser,
                        member
                )
        );
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


    private FitnessClubMember findMember(
            FitnessClub club,
            Integer userId
    ) {

        User user =
                entityManager.find(
                        User.class,
                        userId
                );

        if (user == null) {
            throw new ResourceNotFoundException(
                    "User not found."
            );
        }


        return fitnessClubMemberRepository
                .findByFitnessClubAndUser(
                        club,
                        user
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Club member not found."
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
                    "You do not have permission to manage club members."
            );
        }
    }


    private FitnessClubMember createMember(
            FitnessClub club,
            User user,
            FitnessClubRole role
    ) {

        FitnessClubMemberId id =
                new FitnessClubMemberId();

        id.setClubId(
                club.getId()
        );

        id.setUserId(
                user.getId()
        );


        FitnessClubMember member =
                new FitnessClubMember();

        member.setId(id);
        member.setFitnessClub(club);
        member.setUser(user);
        member.setRole(role);
        member.setJoinedAt(LocalDateTime.now());

        return member;
    }
}