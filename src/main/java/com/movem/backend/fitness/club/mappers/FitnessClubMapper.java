package com.movem.backend.fitness.club.mappers;

import com.movem.backend.fitness.club.dtos.responses.FitnessClubResponse;
import com.movem.backend.shared.attachment.entities.Attachment;
import com.movem.backend.fitness.club.entities.FitnessClub;
import com.movem.backend.shared.attachment.repositories.AttachmentRepository;
import com.movem.backend.fitness.club.repositories.FitnessClubMemberRepository;
import com.movem.backend.shared.attachment.services.GcsFileStorageService;
import com.movem.backend.commons.enums.Fitness.FitnessAttachmentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FitnessClubMapper {

    private final AttachmentRepository attachmentRepository;
    private final GcsFileStorageService gcsFileStorageService;
    private final FitnessClubMemberRepository fitnessClubMemberRepository;

    public FitnessClubResponse toResponse(FitnessClub club) {

        List<Attachment> attachments = attachmentRepository.findByFitnessClubAndDeletedAtIsNull(club);
        long memberCount = fitnessClubMemberRepository.countByFitnessClub(club);

        String profilePic = attachments.stream()
                .filter(a -> a.getAttachmentType() == FitnessAttachmentType.CLUB_PROFILE)
                .map(a -> gcsFileStorageService.publicUrl(a.getFilePath()))
                .findFirst()
                .orElse(null);

        String coverPic = attachments.stream()
                .filter(a -> a.getAttachmentType() == FitnessAttachmentType.CLUB_COVER)
                .map(a -> gcsFileStorageService.publicUrl(a.getFilePath()))
                .findFirst()
                .orElse(null);

        return FitnessClubResponse.builder()
                .id(club.getId())
                .name(club.getName())
                .description(club.getDescription())
                .createdBy(club.getCreatedBy() != null ? club.getCreatedBy().getId() : null)
                .privacy(club.getPrivacy())
                .profilePic(profilePic)
                .coverPic(coverPic)
                .joinToken(club.getJoinToken())
                .createdAt(club.getCreatedAt())
                .updatedAt(club.getUpdatedAt())
                .memberCount(memberCount)
                .build();
    }
}
