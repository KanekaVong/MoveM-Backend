package com.movem.backend.Service.Implement.FitnessServices.Challenge;

import com.movem.backend.Dto.request.FitnessRequest.Challenge.GroupChallenge.CreateGroupChallengeCatalogRequest;
import com.movem.backend.Dto.request.FitnessRequest.Challenge.GroupChallenge.UpdateGroupChallengeCatalogRequest;
import com.movem.backend.Dto.response.FitnessResponse.Challenge.GroupChallengeCatalogResponse;
import com.movem.backend.Entity.Fitness.Challenge.GroupChallengeCatalog;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.FitnessMapper.Challenge.GroupChallengeCatalogMapper;
import com.movem.backend.Repository.FitnessRepository.Challenge.GroupChallengeCatalogRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.FitnessServices.Challenge.GroupChallengeCatalogService;
import com.movem.backend.Service.Event.Factory.Fitness.FitnessChallengeEventFactory;
import com.movem.backend.Service.Event.FeatureEventTrackingService;
import com.movem.backend.model.enums.Fitness.WorkoutType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupChallengeCatalogServiceImpl
        implements GroupChallengeCatalogService {

    private final CurrentUserService currentUserService;
    private final FeatureEventTrackingService featureEventTrackingService;
    private final FitnessChallengeEventFactory fitnessChallengeEventFactory;
    private final GroupChallengeCatalogRepository groupChallengeCatalogRepository;
    private final GroupChallengeCatalogMapper groupChallengeCatalogMapper;


    @Override
    public GroupChallengeCatalogResponse createCatalogChallenge(
            CreateGroupChallengeCatalogRequest request
    ) {

        GroupChallengeCatalog challenge = new GroupChallengeCatalog();

        challenge.setName(request.getName());
        challenge.setWorkoutType(request.getWorkoutType());
        challenge.setTargetValue(request.getTargetValue());
        challenge.setTargetUnit(request.getTargetUnit());
        challenge.setDescription(request.getDescription());
        challenge.setCreatedAt(LocalDateTime.now());
        challenge.setUpdatedAt(LocalDateTime.now());

        GroupChallengeCatalog saved = groupChallengeCatalogRepository.save(challenge);

        featureEventTrackingService.handle(
                fitnessChallengeEventFactory.created(
                        saved,
                        currentUserService.getCurrentUser()
                )
        );

        return groupChallengeCatalogMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public GroupChallengeCatalogResponse getCatalogChallenge(
            Integer catalogId
    ) {

        GroupChallengeCatalog challenge =
                groupChallengeCatalogRepository
                        .findById(catalogId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Group challenge catalog not found."
                                )
                        );

        return groupChallengeCatalogMapper.toResponse(challenge);
    }

    @Override
    @Transactional
    public List<GroupChallengeCatalogResponse>
    getAllCatalogChallenges() {

        return groupChallengeCatalogRepository
                .findAll()
                .stream()
                .map(groupChallengeCatalogMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public List<GroupChallengeCatalogResponse>
    getCatalogChallengesByWorkoutType(
            WorkoutType workoutType
    ) {

        return groupChallengeCatalogRepository
                .findByWorkoutType(
                        workoutType
                )
                .stream()
                .map(groupChallengeCatalogMapper::toResponse)
                .toList();
    }

    @Override
    public GroupChallengeCatalogResponse updateCatalogChallenge(
            Integer catalogId,
            UpdateGroupChallengeCatalogRequest request
    ) {

        GroupChallengeCatalog challenge =
                groupChallengeCatalogRepository
                        .findById(catalogId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Group challenge catalog not found."
                                )
                        );
        String oldName = challenge.getName();

        challenge.setName(request.getName());
        challenge.setWorkoutType(request.getWorkoutType());
        challenge.setTargetValue(request.getTargetValue());
        challenge.setTargetUnit(request.getTargetUnit());
        challenge.setDescription(request.getDescription());
        challenge.setUpdatedAt(LocalDateTime.now());

        GroupChallengeCatalog saved =groupChallengeCatalogRepository.save(challenge);

        featureEventTrackingService.handle(
                fitnessChallengeEventFactory.updated(
                        saved,
                        currentUserService.getCurrentUser(),
                        oldName
                )
        );

        return groupChallengeCatalogMapper.toResponse(saved);}

    @Override
    public void deleteCatalogChallenge(
            Integer catalogId
    ) {

        GroupChallengeCatalog challenge =
                groupChallengeCatalogRepository
                        .findById(catalogId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Group challenge catalog not found."
                                )
                        );

        String oldName = challenge.getName();

        groupChallengeCatalogRepository.delete(challenge);

        featureEventTrackingService.handle(
                fitnessChallengeEventFactory.deleted(
                        challenge,
                        currentUserService.getCurrentUser()
                )
        );
    }
}