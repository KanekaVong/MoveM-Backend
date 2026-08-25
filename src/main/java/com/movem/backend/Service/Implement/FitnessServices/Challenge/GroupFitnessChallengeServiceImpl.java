package com.movem.backend.Service.Implement.FitnessServices.Challenge;

import com.movem.backend.Dto.request.FitnessRequest.Challenge.GroupChallenge.CreateGroupFitnessChallengeFromCatalogRequest;
import com.movem.backend.Dto.request.FitnessRequest.Challenge.GroupChallenge.CreateGroupFitnessChallengeRequest;
import com.movem.backend.Dto.request.FitnessRequest.Challenge.GroupChallenge.UpdateGroupFitnessChallengeRequest;
import com.movem.backend.Dto.response.FitnessResponse.Challenge.GroupFitnessChallengeResponse;
import com.movem.backend.Dto.response.FitnessResponse.Social.SocialChallengeResponse;
import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Fitness.Challenge.FitnessChallengeParticipant;
import com.movem.backend.Entity.Fitness.Challenge.GroupChallengeCatalog;
import com.movem.backend.Entity.Fitness.Challenge.GroupFitnessChallenge;
import com.movem.backend.Entity.Fitness.Club.FitnessClub;
import com.movem.backend.Entity.Fitness.Club.FitnessClubMember;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutSession;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Exception.UnauthorizedActionException;
import com.movem.backend.Mapper.FitnessMapper.Challenge.GroupFitnessChallengeMapper;
import com.movem.backend.Repository.FitnessRepository.Challenge.FitnessChallengeParticipantRepository;
import com.movem.backend.Repository.FitnessRepository.Challenge.GroupChallengeCatalogRepository;
import com.movem.backend.Repository.FitnessRepository.Challenge.GroupFitnessChallengeRepository;
import com.movem.backend.Repository.FitnessRepository.Club.FitnessClubMemberRepository;
import com.movem.backend.Repository.FitnessRepository.Club.FitnessClubRepository;
import com.movem.backend.Repository.FitnessRepository.Workout.FitnessWorkoutSessionRepository;
import com.movem.backend.Repository.SharedRepository.ActivityRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.FitnessServices.Challenge.GroupFitnessChallengeService;
import com.movem.backend.Service.SharedServices.ActivityService;
import com.movem.backend.Service.Event.Factory.Fitness.FitnessChallengeEventFactory;
import com.movem.backend.Service.Event.FeatureEventTrackingService;
import com.movem.backend.Util.FitnessUtil.FitnessChallengeCreateSource;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Activity.ActivityType;
import com.movem.backend.model.enums.Fitness.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupFitnessChallengeServiceImpl
        implements GroupFitnessChallengeService {

    private final GroupFitnessChallengeRepository groupFitnessChallengeRepository;
    private final GroupChallengeCatalogRepository groupChallengeCatalogRepository;
    private final FitnessClubRepository fitnessClubRepository;
    private final FeatureEventTrackingService featureEventTrackingService;
    private final FitnessChallengeEventFactory fitnessChallengeEventFactory;
    private final FitnessWorkoutSessionRepository fitnessWorkoutSessionRepository;
    private final FitnessClubMemberRepository fitnessClubMemberRepository;
    private final GroupFitnessChallengeMapper groupFitnessChallengeMapper;
    private final CurrentUserService currentUserService;
    private final ActivityService activityService;
    private final FitnessChallengeParticipantRepository participantRepository;
    private final ActivityRepository activityRepository;

    @Override
    public GroupFitnessChallengeResponse createChallenge(
            Integer clubId,
            CreateGroupFitnessChallengeRequest request
    ) {

        User currentUser = currentUserService.getCurrentUser();
        FitnessClub club = getClub(clubId);
        FitnessClubMember creatorMembership = getMembership( club, currentUser );
        requireChallengeCreationPermission(creatorMembership);
        GroupChallengeCatalog catalog = null;

        ChallengeValues values = resolveChallengeValues(request, catalog);
        validateDates(values.startAt(), values.endAt());

        LocalDateTime now = LocalDateTime.now();

        FitnessChallengeStatus challengeStatus = determineStatus(
                        values.startAt(), values.endAt(), now);

        FitnessChallengeCreateSource activitySource = new FitnessChallengeCreateSource();

        activitySource.setActivityName(values.name());
        activitySource.setDescription(values.description());
        activitySource.setStartActivity(values.startAt());
        activitySource.setDeadline(values.endAt());
        activitySource.setParentActivityId(null);

        Activity activity = activityService.createActivity(
                        activitySource, currentUser, ActivityType.FITNESS);

        activity.setStatus(mapToActivityStatus(challengeStatus));
        activity.setIsCollaborative(true);
        activity.setUpdatedAt(now);
        activity = activityRepository.save(activity);

        GroupFitnessChallenge challenge = new GroupFitnessChallenge();

        challenge.setActivity(activity);
        challenge.setFitnessClub(club);
        challenge.setName( values.name() );
        challenge.setWorkoutType(values.workoutType());
        challenge.setTargetValue(values.targetValue());
        challenge.setTargetUnit(values.targetUnit());
        challenge.setDescription(values.description());
        challenge.setCatalog(null);
        challenge.setChallengeSource(ChallengeSource.CUSTOM);
        challenge.setCreatedBy(currentUser);
        challenge.setStartAt(values.startAt());
        challenge.setEndAt(values.endAt());
        challenge.setStatus(challengeStatus);
        challenge.setCreatedAt(now);
        challenge.setUpdatedAt(now);

        GroupFitnessChallenge saved = groupFitnessChallengeRepository.save(challenge);

        featureEventTrackingService.handle(
                fitnessChallengeEventFactory.created(saved, currentUser)
        );

        return groupFitnessChallengeMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public GroupFitnessChallengeResponse getChallenge(
            Integer challengeId
    ) {

        GroupFitnessChallenge challenge =
                groupFitnessChallengeRepository
                        .findById(challengeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Group fitness challenge not found."
                                )
                        );

        return groupFitnessChallengeMapper.toResponse(challenge);
    }

    @Override
    @Transactional
    public List<GroupFitnessChallengeResponse>
    getClubChallenges(
            Integer clubId
    ) {

        FitnessClub club = getClub(clubId);

        return groupFitnessChallengeRepository
                .findByFitnessClubOrderByCreatedAtDesc(
                        club
                )
                .stream()
                .map(
                        groupFitnessChallengeMapper::toResponse
                )
                .toList();
    }

    @Override
    @Transactional
    public List<GroupFitnessChallengeResponse>
    getMyCreatedChallenges() {

        User currentUser = currentUserService.getCurrentUser();

        return groupFitnessChallengeRepository
                .findByCreatedByOrderByCreatedAtDesc(
                        currentUser
                )
                .stream()
                .map(
                        groupFitnessChallengeMapper::toResponse
                )
                .toList();
    }

    @Override
    public GroupFitnessChallengeResponse updateChallenge(
            Integer challengeId,
            UpdateGroupFitnessChallengeRequest request
    ) {

        User currentUser = currentUserService.getCurrentUser();

        GroupFitnessChallenge challenge =
                groupFitnessChallengeRepository
                        .findById(challengeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Group fitness challenge not found."
                                )
                        );


        FitnessClubMember membership = getMembership(
                        challenge.getFitnessClub(), currentUser);

        requireChallengeManagementPermission(
                        membership,
                challenge);

        validateDates( request.getStartAt(), request.getEndAt());


        challenge.setName(request.getName());
        challenge.setWorkoutType(request.getWorkoutType());
        challenge.setTargetValue(request.getTargetValue());
        challenge.setTargetUnit(request.getTargetUnit());
        challenge.setDescription(request.getDescription());
        challenge.setStartAt(request.getStartAt());
        challenge.setEndAt(request.getEndAt());

        FitnessChallengeStatus status =
                determineStatus(
                        request.getStartAt(),
                        request.getEndAt(),
                        LocalDateTime.now()
                );

        challenge.setStatus(status);
        Activity activity = challenge.getActivity();

        if (activity != null) {
            activity.setActivityName(
                    request.getName()
            );
            activity.setDescription(
                    request.getDescription()
            );
            activity.setStartActivity(
                    request.getStartAt()
            );
            activity.setDeadline(
                    request.getEndAt()
            );
            activity.setStatus(
                    mapToActivityStatus(status)
            );
            activity.setUpdatedAt(
                    LocalDateTime.now()
            );
            activityRepository.save(activity);
        }


        challenge.setUpdatedAt(
                LocalDateTime.now()
        );


        GroupFitnessChallenge saved =
                groupFitnessChallengeRepository.save(
                        challenge
                );

        featureEventTrackingService.handle(
                fitnessChallengeEventFactory.updated(saved, currentUser)
        );

        return groupFitnessChallengeMapper
                .toResponse(saved);
    }

    @Override
    public void deleteChallenge(
            Integer challengeId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        GroupFitnessChallenge challenge =
                groupFitnessChallengeRepository
                        .findById(challengeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Group fitness challenge not found."
                                )
                        );


        FitnessClubMember membership =
                getMembership(
                        challenge.getFitnessClub(),
                        currentUser
                );

        requireChallengeManagementPermission(
                membership,
                challenge
        );

        Activity activity =
                challenge.getActivity();

        if (activity != null) {

            activity.setStatus(
                    ActivityStatus.CANCELLED
            );

            activity.setUpdatedAt(
                    LocalDateTime.now()
            );

            activityRepository.save(activity);
        }


        challenge.setStatus(
                FitnessChallengeStatus.CANCELLED
        );

        challenge.setUpdatedAt(
                LocalDateTime.now()
        );

        groupFitnessChallengeRepository.save(
                challenge
        );

        featureEventTrackingService.handle(
                fitnessChallengeEventFactory.cancelled(challenge, currentUser)
        );
    }

    @Override
    public GroupFitnessChallengeResponse createChallengeFromCatalog(
            Integer clubId,
            Integer catalogId,
            CreateGroupFitnessChallengeFromCatalogRequest request
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

        FitnessClubMember membership =
                fitnessClubMemberRepository
                        .findByFitnessClubAndUser(
                                club,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new UnauthorizedActionException(
                                        "You are not a member of this fitness club."
                                )
                        );

        if (
                membership.getRole()
                        != FitnessClubRole.OWNER
                        &&
                        membership.getRole()
                                != FitnessClubRole.ADMIN
        ) {

            throw new UnauthorizedActionException(
                    "You do not have permission to create a fitness challenge."
            );
        }


        GroupChallengeCatalog catalog =
                groupChallengeCatalogRepository
                        .findById(catalogId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Group challenge catalog not found."
                                )
                        );


        validateDates(
                request.getStartAt(),
                request.getEndAt()
        );


        LocalDateTime now =
                LocalDateTime.now();


        FitnessChallengeStatus status =
                determineStatus(
                        request.getStartAt(),
                        request.getEndAt(),
                        now
                );

        FitnessChallengeCreateSource activitySource =
                new FitnessChallengeCreateSource();

        activitySource.setActivityName(
                catalog.getName()
        );

        activitySource.setDescription(
                catalog.getDescription()
        );

        activitySource.setStartActivity(
                request.getStartAt()
        );

        activitySource.setDeadline(
                request.getEndAt()
        );

        activitySource.setParentActivityId(
                null
        );


        Activity activity =
                activityService.createActivity(
                        activitySource,
                        currentUser,
                        ActivityType.FITNESS
                );


        activity.setStatus(
                mapToActivityStatus(status)
        );

        activity.setIsCollaborative(true);
        activity.setUpdatedAt(now);

        activity =
                activityRepository.save(
                        activity
                );

        GroupFitnessChallenge challenge =
                new GroupFitnessChallenge();

        challenge.setActivity(
                activity
        );

        challenge.setFitnessClub(
                club
        );

        challenge.setCatalog(
                catalog
        );

        challenge.setChallengeSource(
                ChallengeSource.RECOMMENDED
        );

        challenge.setName(
                catalog.getName()
        );

        challenge.setWorkoutType(
                catalog.getWorkoutType()
        );

        challenge.setTargetValue(
                catalog.getTargetValue()
        );

        challenge.setTargetUnit(
                catalog.getTargetUnit()
        );

        challenge.setDescription(
                catalog.getDescription()
        );

        challenge.setCreatedBy(
                currentUser
        );

        challenge.setStartAt(
                request.getStartAt()
        );

        challenge.setEndAt(
                request.getEndAt()
        );

        challenge.setStatus(
                status
        );

        challenge.setCreatedAt(now);
        challenge.setUpdatedAt(now);


        GroupFitnessChallenge saved =
                groupFitnessChallengeRepository.save(
                        challenge
                );

        featureEventTrackingService.handle(
                fitnessChallengeEventFactory.created(saved, currentUser)
        );

        return groupFitnessChallengeMapper
                .toResponse(saved);
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
                                "You are not a member of this fitness club."
                        )
                );
    }

    @Override
    @Transactional
    public SocialChallengeResponse getSocialChallenge(
            Integer challengeId
    ) {

        User currentUser = currentUserService.getCurrentUser();

        GroupFitnessChallenge challenge =
                groupFitnessChallengeRepository.findById(challengeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Group fitness challenge not found."
                                )
                        );

        boolean isOwner =
                challenge.getCreatedBy()
                        .getId()
                        .equals(currentUser.getId());

        boolean isParticipant =
                participantRepository
                        .findByChallengeAndUser(
                                challenge,
                                currentUser
                        )
                        .isPresent();

        if (!isOwner && !isParticipant) {
            throw new UnauthorizedActionException(
                    "You do not have access to this challenge."
            );
        }

        long participantCount =
                participantRepository.countByChallenge(
                        challenge
                );

        long completedParticipants =
                participantRepository
                        .findByChallenge(challenge)
                        .stream()
                        .filter(participant ->
                                participant.getStatus()
                                        == FitnessChallengeParticipantStatus.COMPLETED
                        )
                        .count();

        FitnessChallengeParticipant myParticipant =
                participantRepository
                        .findByChallengeAndUser(
                                challenge,
                                currentUser
                        )
                        .orElse(null);

        BigDecimal myProgress =
                myParticipant != null
                        ? calculateProgress(myParticipant)
                        : BigDecimal.ZERO;

        boolean myCompleted =
                myParticipant != null
                        && myParticipant.getStatus()
                        == FitnessChallengeParticipantStatus.COMPLETED;

        return SocialChallengeResponse.builder()
                .challengeId(challenge.getId())
                .name(challenge.getName())
                .description(challenge.getDescription())
                .workoutType(challenge.getWorkoutType().name())
                .targetValue(challenge.getTargetValue())
                .targetUnit(challenge.getTargetUnit().name())
                .status(challenge.getStatus())
                .startAt(challenge.getStartAt())
                .endAt(challenge.getEndAt())
                .creatorId(challenge.getCreatedBy().getId())
                .creatorUsername(challenge.getCreatedBy().getUsername())
                .participantCount(participantCount)
                .completedParticipants(completedParticipants)
                .myProgress(myProgress)
                .myCompleted(myCompleted)
                .build();
    }

    private void requireChallengeCreationPermission(
            FitnessClubMember membership
    ) {

        if (
                membership.getRole()
                        != FitnessClubRole.OWNER
                        &&
                        membership.getRole()
                                != FitnessClubRole.ADMIN
        ) {

            throw new IllegalArgumentException(
                    "You do not have permission to create fitness challenges in this club."
            );
        }
    }


    private void requireChallengeManagementPermission(
            FitnessClubMember membership,
            GroupFitnessChallenge challenge
    ) {

        boolean isOwnerOrAdmin =
                membership.getRole()
                        == FitnessClubRole.OWNER
                        ||
                        membership.getRole()
                                == FitnessClubRole.ADMIN;

        boolean isCreator =
                challenge.getCreatedBy()
                        .getId()
                        .equals(
                                membership.getUser().getId()
                        );

        if (!isOwnerOrAdmin && !isCreator) {

            throw new IllegalArgumentException(
                    "You do not have permission to manage this challenge."
            );
        }
    }


    private void validateDates(
            LocalDateTime startAt,
            LocalDateTime endAt
    ) {

        if (startAt == null || endAt == null) {

            throw new IllegalArgumentException(
                    "Start and end time are required."
            );
        }

        if (!endAt.isAfter(startAt)) {

            throw new IllegalArgumentException(
                    "End time must be after start time."
            );
        }
    }


    private FitnessChallengeStatus determineStatus(
            LocalDateTime startAt,
            LocalDateTime endAt,
            LocalDateTime now
    ) {

        if (!now.isBefore(startAt)) {

            if (!now.isBefore(endAt)) {
                return FitnessChallengeStatus.COMPLETE;
            }

            return FitnessChallengeStatus.IN_PROGRESS;
        }

        return FitnessChallengeStatus.UPCOMING;
    }


    private ActivityStatus mapToActivityStatus(
            FitnessChallengeStatus status
    ) {

        return switch (status) {

            case UPCOMING -> ActivityStatus.UPCOMING;

            case IN_PROGRESS -> ActivityStatus.IN_PROGRESS;

            case COMPLETE -> ActivityStatus.COMPLETE;

            case CANCELLED -> ActivityStatus.CANCELLED;
        };
    }


    private ChallengeValues resolveChallengeValues(
            CreateGroupFitnessChallengeRequest request,
            GroupChallengeCatalog catalog
    ) {

        if (catalog != null) {

            return new ChallengeValues(
                    catalog.getName(),
                    catalog.getWorkoutType(),
                    catalog.getTargetValue(),
                    catalog.getTargetUnit(),
                    catalog.getDescription(),
                    request.getStartAt(),
                    request.getEndAt()
            );
        }

        return new ChallengeValues(
                request.getName(),
                request.getWorkoutType(),
                request.getTargetValue(),
                request.getTargetUnit(),
                request.getDescription(),
                request.getStartAt(),
                request.getEndAt()
        );
    }


    private record ChallengeValues(
            String name,
            com.movem.backend.model.enums.Fitness.WorkoutType workoutType,
            java.math.BigDecimal targetValue,
            com.movem.backend.model.enums.Fitness.ChallengeTargetUnit targetUnit,
            String description,
            LocalDateTime startAt,
            LocalDateTime endAt
    ) {
    }

    private BigDecimal calculateProgress(
            FitnessChallengeParticipant participant
    ) {

        List<FitnessWorkoutSession> sessions =
                fitnessWorkoutSessionRepository
                        .findByGroupChallengeParticipant(
                                participant
                        );

        return sessions.stream()
                .filter(session ->
                        session.getStatus()
                                == FitnessWorkoutStatus.COMPLETED
                )
                .map(FitnessWorkoutSession::getDistance)
                .filter(Objects::nonNull)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }


}