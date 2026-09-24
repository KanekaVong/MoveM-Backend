package com.movem.backend.fitness.challenges.services.impl;

import com.movem.backend.fitness.challenges.dtos.requests.SoloChallenge.CreateSoloChallengeRequest;
import com.movem.backend.fitness.challenges.dtos.requests.SoloChallenge.UpdateSoloChallengeRequest;
import com.movem.backend.fitness.challenges.dtos.responses.SoloChallengeResponse;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.fitness.challenges.entities.SoloChallenge;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.fitness.challenges.mappers.SoloChallengeMapper;
import com.movem.backend.fitness.challenges.repositories.SoloChallengeCatalogRepository;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.fitness.challenges.services.SoloChallengeService;
import com.movem.backend.commons.Event.Factory.Fitness.FitnessChallengeEventFactory;
import com.movem.backend.shared.historyandlogs.featureevents.services.FeatureEventTrackingService;
import com.movem.backend.commons.enums.Fitness.WorkoutType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SoloChallengeServiceImpl
        implements SoloChallengeService {

    private final CurrentUserService currentUserService;
    private final FeatureEventTrackingService featureEventTrackingService;
    private final FitnessChallengeEventFactory fitnessChallengeEventFactory;
    private final SoloChallengeCatalogRepository soloChallengeRepository;
    private final SoloChallengeMapper soloChallengeMapper;

    @Override
    public List<SoloChallengeResponse> getAllChallenges() {

        return soloChallengeRepository
                .findAll()
                .stream()
                .map(soloChallengeMapper::toResponse)
                .toList();
    }

    @Override
    public SoloChallengeResponse getChallenge(
            Integer challengeId
    ) {

        SoloChallenge challenge =
                soloChallengeRepository
                        .findById(challengeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Solo challenge not found."
                                )
                        );

        return soloChallengeMapper
                .toResponse(challenge);
    }

    @Override
    public List<SoloChallengeResponse> getChallengesByWorkoutType(
            WorkoutType workoutType
    ) {

        return soloChallengeRepository
                .findByWorkoutType(workoutType)
                .stream()
                .map(soloChallengeMapper::toResponse)
                .toList();
    }

    @Override
    public SoloChallengeResponse createChallenge(
            CreateSoloChallengeRequest request
    ) {

        SoloChallenge challenge =
                new SoloChallenge();

        challenge.setName(
                request.getName()
        );

        challenge.setWorkoutType(
                request.getType()
        );

        challenge.setWorkoutLevel(
                request.getWorkoutLevel()
        );

        challenge.setTargetValue(
                request.getTargetValue()
        );

        challenge.setTargetUnit(
                request.getTargetUnit()
        );

        challenge.setDescription(
                request.getDescription()
        );

        challenge.setCreatedAt(
                LocalDateTime.now()
        );

        challenge.setUpdatedAt(
                LocalDateTime.now()
        );

        SoloChallenge saved = soloChallengeRepository.save(challenge);

        featureEventTrackingService.handle(
                fitnessChallengeEventFactory.soloChallengeCreated(
                        saved,
                        currentUserService.getCurrentUser()
                )
        );

        return soloChallengeMapper.toResponse(saved);
    }

    @Override
    public SoloChallengeResponse updateChallenge(
            Integer challengeId,
            UpdateSoloChallengeRequest request
    ) {

        SoloChallenge challenge =
                soloChallengeRepository
                        .findById(challengeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Solo challenge not found."
                                )
                        );

        String oldName = challenge.getName();

        challenge.setName(request.getName());
        challenge.setWorkoutType(request.getType());
        challenge.setWorkoutLevel(request.getWorkoutLevel());
        challenge.setTargetValue(request.getTargetValue());
        challenge.setTargetUnit(request.getTargetUnit());
        challenge.setDescription(request.getDescription());
        challenge.setUpdatedAt(LocalDateTime.now());

        SoloChallenge saved =
                soloChallengeRepository.save(challenge);

        featureEventTrackingService.handle(
                fitnessChallengeEventFactory.soloChallengeUpdated(
                        saved,
                        currentUserService.getCurrentUser(),
                        oldName
                )
        );

        return soloChallengeMapper.toResponse(saved);
    }

    @Override
    public void deleteChallenge(
            Integer challengeId
    ) {

        User currentUser = currentUserService.getCurrentUser();

        SoloChallenge challenge = soloChallengeRepository.findById(challengeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Solo challenge not found."));

        soloChallengeRepository.delete(challenge);

        featureEventTrackingService.handle(
                fitnessChallengeEventFactory.soloChallengeDeleted(
                        challenge,
                        currentUser
                )
        );
    }
}
