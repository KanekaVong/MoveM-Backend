package com.movem.backend.fitness.challenges.mappers;

import com.movem.backend.fitness.challenges.dtos.responses.GroupFitnessChallengeResponse;
import com.movem.backend.fitness.challenges.entities.GroupFitnessChallenge;
import com.movem.backend.shared.attachment.repositories.AttachmentRepository;
import com.movem.backend.shared.attachment.services.GcsFileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupFitnessChallengeMapper {
    private final AttachmentRepository attachmentRepository;
    private final GcsFileStorageService gcsFileStorageService;

    public GroupFitnessChallengeResponse toResponse(GroupFitnessChallenge challenge) {

        String imageUrl = attachmentRepository
                .findByGroupFitnessChallengeAndDeletedAtIsNull(challenge)
                .stream()
                .findFirst()
                .map(a -> gcsFileStorageService.publicUrl(a.getFilePath()))
                .orElse(null);

        return GroupFitnessChallengeResponse.builder()
                .id(challenge.getId())
                .clubId(challenge.getFitnessClub() != null ? challenge.getFitnessClub().getId() : null)
                .createdBy(challenge.getCreatedBy() != null ? challenge.getCreatedBy().getId() : null)
                .name(challenge.getName())
                .workoutType(challenge.getWorkoutType())
                .targetValue(challenge.getTargetValue())
                .targetUnit(challenge.getTargetUnit())
                .description(challenge.getDescription())
                .catalogId(challenge.getCatalog() != null ? challenge.getCatalog().getId() : null)
                .challengeSource(challenge.getChallengeSource())
                .startAt(challenge.getStartAt())
                .endAt(challenge.getEndAt())
                .status(challenge.getStatus())
                .createdAt(challenge.getCreatedAt())
                .updatedAt(challenge.getUpdatedAt())
                .imageUrl(imageUrl)
                .build();
    }
}