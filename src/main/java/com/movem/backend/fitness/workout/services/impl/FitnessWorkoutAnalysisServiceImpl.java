package com.movem.backend.fitness.workout.services.impl;

import com.movem.backend.fitness.workout.dtos.requests.FitnessWorkoutAnalysisRequest;
import com.movem.backend.fitness.workout.dtos.responses.FitnessWorkoutAnalysisResponse;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutAnalysis;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutSession;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.fitness.workout.repositories.FitnessWorkoutAnalysisRepository;
import com.movem.backend.fitness.workout.repositories.FitnessWorkoutSessionRepository;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.fitness.workout.services.FitnessWorkoutAnalysisService;
import com.movem.backend.commons.enums.Fitness.TrackingMode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FitnessWorkoutAnalysisServiceImpl
        implements FitnessWorkoutAnalysisService {

    private final FitnessWorkoutAnalysisRepository analysisRepository;
    private final FitnessWorkoutSessionRepository workoutSessionRepository;
    private final CurrentUserService currentUserService;
    private final ObjectMapper objectMapper;

    @Override
    public FitnessWorkoutAnalysisResponse saveAnalysis(
            Integer sessionId,
            FitnessWorkoutAnalysisRequest request
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                workoutSessionRepository
                        .findByIdAndUser(
                                sessionId,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout session not found."
                                )
                        );

        if (session.getTrackingMode() != TrackingMode.POSE) {
            throw new IllegalArgumentException(
                    "Workout session does not use pose tracking."
            );
        }

        FitnessWorkoutAnalysis analysis =
                analysisRepository
                        .findByWorkoutSession(session)
                        .orElseGet(FitnessWorkoutAnalysis::new);

        analysis.setWorkoutSession(session);
        analysis.setExercise(request.getExercise());
        analysis.setReps(request.getReps());
        analysis.setValidReps(request.getValidReps());
        analysis.setInvalidReps(request.getInvalidReps());
        analysis.setFormScore(request.getFormScore());

        try {
            analysis.setFeedback(
                    objectMapper.writeValueAsString(
                            request.getFeedback()
                    )
            );
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                    "Unable to process workout feedback."
            );
        }

        if (analysis.getCreatedAt() == null) {
            analysis.setCreatedAt(LocalDateTime.now());
        }

        analysis.setUpdatedAt(LocalDateTime.now());

        FitnessWorkoutAnalysis saved =
                analysisRepository.save(analysis);

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public FitnessWorkoutAnalysisResponse getAnalysis(
            Integer sessionId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessWorkoutSession session =
                workoutSessionRepository
                        .findByIdAndUser(
                                sessionId,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout session not found."
                                )
                        );

        FitnessWorkoutAnalysis analysis =
                analysisRepository
                        .findByWorkoutSession(session)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout analysis not found."
                                )
                        );

        return toResponse(analysis);
    }

    private FitnessWorkoutAnalysisResponse toResponse(
            FitnessWorkoutAnalysis analysis
    ) {

        List<String> feedback = List.of();

        if (analysis.getFeedback() != null
                && !analysis.getFeedback().isBlank()) {

            try {
                feedback = objectMapper.readValue(
                        analysis.getFeedback(),
                        new TypeReference<List<String>>() {}
                );
            } catch (JsonProcessingException e) {
                throw new IllegalStateException(
                        "Unable to read workout feedback."
                );
            }
        }

        return FitnessWorkoutAnalysisResponse.builder()
                .id(analysis.getId())
                .sessionId(
                        analysis.getWorkoutSession().getId()
                )
                .exercise(analysis.getExercise())
                .reps(analysis.getReps())
                .validReps(analysis.getValidReps())
                .invalidReps(analysis.getInvalidReps())
                .formScore(analysis.getFormScore())
                .feedback(feedback)
                .createdAt(analysis.getCreatedAt())
                .updatedAt(analysis.getUpdatedAt())
                .build();
    }
}