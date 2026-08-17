package com.movem.backend.Service.Implement.FitnessServices.Challenge;

import com.movem.backend.Dto.request.FitnessRequest.Challenge.SoloChallenge.CreateSoloChallengeRequest;
import com.movem.backend.Dto.request.FitnessRequest.Challenge.SoloChallenge.UpdateSoloChallengeRequest;
import com.movem.backend.Dto.response.FitnessResponse.Challenge.SoloChallengeResponse;
import com.movem.backend.Entity.Fitness.Challenge.SoloChallenge;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.FitnessMapper.Challenge.SoloChallengeMapper;
import com.movem.backend.Repository.FitnessRepository.Challenge.SoloChallengeCatalogRepository;
import com.movem.backend.Service.FitnessServices.Challenge.SoloChallengeService;
import com.movem.backend.model.enums.Fitness.WorkoutType;
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

    private final SoloChallengeCatalogRepository
            soloChallengeRepository;

    private final SoloChallengeMapper
            soloChallengeMapper;

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

        SoloChallenge saved =
                soloChallengeRepository.save(
                        challenge
                );

        return soloChallengeMapper
                .toResponse(saved);
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

        challenge.setUpdatedAt(
                LocalDateTime.now()
        );

        SoloChallenge saved =
                soloChallengeRepository.save(
                        challenge
                );

        return soloChallengeMapper
                .toResponse(saved);
    }

    @Override
    public void deleteChallenge(
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

        soloChallengeRepository.delete(
                challenge
        );
    }
}
