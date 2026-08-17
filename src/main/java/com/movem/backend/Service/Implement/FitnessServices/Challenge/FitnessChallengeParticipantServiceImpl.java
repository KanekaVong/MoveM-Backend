package com.movem.backend.Service.Implement.FitnessServices.Challenge;

import com.movem.backend.Dto.response.FitnessResponse.Challenge.FitnessChallengeParticipantResponse;
import com.movem.backend.Entity.Fitness.Challenge.FitnessChallengeParticipant;
import com.movem.backend.Entity.Fitness.Challenge.GroupFitnessChallenge;
import com.movem.backend.Entity.Fitness.Club.FitnessClub;
import com.movem.backend.Entity.Fitness.Club.FitnessClubMember;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.FitnessMapper.Challenge.FitnessChallengeParticipantMapper;
import com.movem.backend.Repository.FitnessRepository.Challenge.FitnessChallengeParticipantRepository;
import com.movem.backend.Repository.FitnessRepository.Challenge.GroupFitnessChallengeRepository;
import com.movem.backend.Repository.FitnessRepository.Club.FitnessClubMemberRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.FitnessServices.Challenge.FitnessChallengeParticipantService;
import com.movem.backend.model.enums.Fitness.FitnessChallengeParticipantStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FitnessChallengeParticipantServiceImpl
        implements FitnessChallengeParticipantService {

    private final FitnessChallengeParticipantRepository
            participantRepository;

    private final GroupFitnessChallengeRepository
            challengeRepository;

    private final FitnessClubMemberRepository
            clubMemberRepository;

    private final FitnessChallengeParticipantMapper
            participantMapper;

    private final CurrentUserService
            currentUserService;


    @Override
    public FitnessChallengeParticipantResponse joinChallenge(
            Integer challengeId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        GroupFitnessChallenge challenge =
                getChallenge(challengeId);

        FitnessClub club =
                challenge.getFitnessClub();

        if (club == null) {
            throw new IllegalStateException(
                    "Fitness challenge is not associated with a club."
            );
        }


        /*
         * User must already belong to the club.
         */

        FitnessClubMember clubMember =
                clubMemberRepository
                        .findByFitnessClubAndUser(
                                club,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "You must be a member of the fitness club before joining this challenge."
                                )
                        );

        var existingParticipation =
                participantRepository
                        .findByChallengeAndUser(
                                challenge,
                                currentUser
                        );

        if (existingParticipation.isPresent()) {

            FitnessChallengeParticipant existing =
                    existingParticipation.get();

            if (
                    existing.getStatus()
                            == FitnessChallengeParticipantStatus.ACTIVE
            ) {

                throw new IllegalArgumentException(
                        "You are already participating in this challenge."
                );
            }

            if (
                    existing.getStatus()
                            == FitnessChallengeParticipantStatus.LEFT
            ) {

                existing.setStatus(
                        FitnessChallengeParticipantStatus.ACTIVE
                );

                existing.setJoinedAt(
                        LocalDateTime.now()
                );

                existing.setCompletedAt(
                        null
                );

                FitnessChallengeParticipant saved =
                        participantRepository.save(
                                existing
                        );

                return participantMapper.toResponse(
                        saved
                );
            }

            throw new IllegalArgumentException(
                    "You cannot join this challenge again."
            );
        }

        switch (challenge.getStatus()) {

            case COMPLETE ->
                    throw new IllegalArgumentException(
                            "You cannot join a completed challenge."
                    );

            case CANCELLED ->
                    throw new IllegalArgumentException(
                            "You cannot join a cancelled challenge."
                    );

            default -> {

            }
        }


        FitnessChallengeParticipant participant =
                new FitnessChallengeParticipant();

        participant.setChallenge(
                challenge
        );

        participant.setUser(
                currentUser
        );

        participant.setStatus(
                FitnessChallengeParticipantStatus.ACTIVE
        );

        participant.setJoinedAt(
                LocalDateTime.now()
        );

        participant.setCompletedAt(
                null
        );


        FitnessChallengeParticipant saved =
                participantRepository.save(
                        participant
                );

        return participantMapper.toResponse(
                saved
        );
    }


    @Override
    @Transactional(readOnly = true)
    public FitnessChallengeParticipantResponse getParticipant(
            Integer participantId
    ) {

        FitnessChallengeParticipant participant =
                participantRepository
                        .findById(participantId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Challenge participant not found."
                                )
                        );

        return participantMapper.toResponse(
                participant
        );
    }
    @Override
    @Transactional(readOnly = true)
    public FitnessChallengeParticipantResponse
    getMyParticipation(
            Integer challengeId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        GroupFitnessChallenge challenge =
                getChallenge(challengeId);

        FitnessChallengeParticipant participant =
                participantRepository
                        .findByChallengeAndUser(
                                challenge,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "You are not a participant in this challenge."
                                )
                        );

        return participantMapper.toResponse(
                participant
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<FitnessChallengeParticipantResponse>
    getChallengeParticipants(
            Integer challengeId
    ) {

        GroupFitnessChallenge challenge =
                getChallenge(challengeId);

        return participantRepository
                .findByChallenge(
                        challenge
                )
                .stream()
                .map(
                        participantMapper::toResponse
                )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FitnessChallengeParticipantResponse>
    getMyParticipations() {

        User currentUser =
                currentUserService.getCurrentUser();

        return participantRepository
                .findByUser(
                        currentUser
                )
                .stream()
                .map(
                        participantMapper::toResponse
                )
                .toList();
    }

    @Override
    public void leaveChallenge(
            Integer challengeId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        GroupFitnessChallenge challenge =
                getChallenge(challengeId);

        FitnessChallengeParticipant participant =
                participantRepository
                        .findByChallengeAndUser(
                                challenge,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "You are not a participant in this challenge."
                                )
                        );


        if (
                participant.getStatus()
                        == FitnessChallengeParticipantStatus.COMPLETED
        ) {

            throw new IllegalArgumentException(
                    "You cannot leave a completed challenge."
            );
        }


        if (
                participant.getStatus()
                        == FitnessChallengeParticipantStatus.LEFT
        ) {

            throw new IllegalArgumentException(
                    "You have already left this challenge."
            );
        }


        participant.setStatus(
                FitnessChallengeParticipantStatus.LEFT
        );

        participantRepository.save(
                participant
        );
    }

    private GroupFitnessChallenge getChallenge(
            Integer challengeId
    ) {

        return challengeRepository
                .findById(challengeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Group fitness challenge not found."
                        )
                );
    }
}