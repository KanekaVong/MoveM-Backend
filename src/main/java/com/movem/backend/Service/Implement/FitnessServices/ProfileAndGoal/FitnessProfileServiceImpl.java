package com.movem.backend.Service.Implement.FitnessServices.ProfileAndGoal;

import com.movem.backend.Dto.request.FitnessRequest.ProfileAndGoal.CreateFitnessProfileRequest;
import com.movem.backend.Dto.request.FitnessRequest.ProfileAndGoal.UpdateFitnessProfileRequest;
import com.movem.backend.Dto.response.FitnessResponse.ProfileAndGoal.FitnessGoalResponse;
import com.movem.backend.Dto.response.FitnessResponse.ProfileAndGoal.FitnessProfileResponse;
import com.movem.backend.Entity.Fitness.ProfileAndGoal.FitnessProfile;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.DuplicateResourceException;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.FitnessMapper.ProfileAndGoal.FitnessProfileMapper;
import com.movem.backend.Repository.FitnessRepository.ProfileAndGoal.FitnessGoalRepository;
import com.movem.backend.Repository.FitnessRepository.ProfileAndGoal.FitnessProfileRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.FitnessServices.ProfileAndGoal.FitnessProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional
public class FitnessProfileServiceImpl
        implements FitnessProfileService {

    private final FitnessProfileRepository fitnessProfileRepository;

    private final CurrentUserService currentUserService;

    private final FitnessGoalRepository fitnessGoalRepository;

    private final FitnessProfileMapper fitnessProfileMapper;

    @Override
    public FitnessProfileResponse createProfile(
            CreateFitnessProfileRequest request
    ){
        User currentUser = currentUserService.getCurrentUser();

        if(fitnessProfileRepository.existsById(
                currentUser.getId()
                )) {
            throw new DuplicateResourceException(
                    "Fitness profile already exists."
            );
        }

        FitnessProfile profile =
                new FitnessProfile();

         profile.setUser(currentUser);

        profile.setHeight(request.getHeight());
        profile.setWeight(request.getWeight());

        profile.setBmi(
                calculateBmi(
                        request.getWeight(),
                        request.getHeight()
                )
        );

        profile.setUpdatedAt(
                LocalDateTime.now()
        );

        FitnessProfile saved =
                fitnessProfileRepository.save(profile);

        return fitnessProfileMapper.toResponse(
                saved,
                getFitnessGoal(currentUser)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public FitnessProfileResponse getMyProfile() {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessProfile profile =
                fitnessProfileRepository.findById(
                                currentUser.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Fitness profile not found."
                                )
                        );

        return fitnessProfileMapper.toResponse(
                profile,
                getFitnessGoal(currentUser)
        );
    }


    @Override
    public FitnessProfileResponse updateProfile(
            UpdateFitnessProfileRequest request
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessProfile profile =
                fitnessProfileRepository.findById(
                                currentUser.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Fitness profile not found."
                                )
                        );

        if (request.getHeight() != null) {

            profile.setHeight(
                    request.getHeight()
            );
        }

        if (request.getWeight() != null) {

            profile.setWeight(
                    request.getWeight()
            );
        }

        profile.setBmi(
                calculateBmi(
                        profile.getWeight(),
                        profile.getHeight()
                )
        );

        profile.setUpdatedAt(
                LocalDateTime.now()
        );

        FitnessProfile saved =
                fitnessProfileRepository.save(profile);

        return fitnessProfileMapper.toResponse(
                saved,
                getFitnessGoal(currentUser)
        );
    }


    @Override
    public void deleteProfile() {

        User currentUser =
                currentUserService.getCurrentUser();

        FitnessProfile profile =
                fitnessProfileRepository.findById(
                                currentUser.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Fitness profile not found."
                                )
                        );

        fitnessProfileRepository.delete(profile);
    }


    private BigDecimal calculateBmi(
            BigDecimal weight,
            BigDecimal height
    ) {

        if (weight == null || height == null) {

            return null;
        }

        if (height.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Height must be greater than zero."
            );
        }

        if (weight.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Weight must be greater than zero."
            );
        }

        BigDecimal heightInMeters =
                height.divide(
                        BigDecimal.valueOf(100),
                        10,
                        RoundingMode.HALF_UP
                );

        return weight
                .divide(
                        heightInMeters.multiply(heightInMeters),
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private FitnessGoalResponse getFitnessGoal(
            User currentUser
    ) {

        return fitnessGoalRepository
                .findFirstByUserAndStatusOrderByCreatedAtDesc(
                        currentUser,
                        "ACTIVE"
                )
                .map(goal ->
                        FitnessGoalResponse.builder()
                                .id(goal.getId())
                                .userId(goal.getUser().getId())
                                .goalType(goal.getGoalType())
                                .targetWeight(goal.getTargetWeight())
                                .targetTimeline(goal.getTargetTimeline())
                                .workoutLevel(goal.getWorkoutLevel())
                                .estimatedWeightChange(
                                        goal.getEstimatedWeightChange()
                                )
                                .estimatedDailyDeficit(
                                        goal.getEstimatedDailyDeficit()
                                )
                                .status(goal.getStatus())
                                .createdAt(goal.getCreatedAt())
                                .updatedAt(goal.getUpdatedAt())
                                .build()
                )
                .orElse(null);
    }
}