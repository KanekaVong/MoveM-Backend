package com.movem.backend.fitness.club.services.impl;

import com.movem.backend.fitness.club.dtos.requests.CreateFitnessClubRequest;
import com.movem.backend.fitness.club.dtos.requests.UpdateFitnessClubRequest;
import com.movem.backend.fitness.club.dtos.responses.FitnessClubResponse;
import com.movem.backend.shared.attachment.entities.Attachment;
import com.movem.backend.fitness.club.entities.FitnessClub;
import com.movem.backend.fitness.club.entities.FitnessClubMember;
import com.movem.backend.fitness.club.entities.FitnessClubMemberId;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.fitness.club.mappers.FitnessClubMapper;
import com.movem.backend.shared.attachment.repositories.AttachmentRepository;
import com.movem.backend.fitness.club.repositories.FitnessClubMemberRepository;
import com.movem.backend.fitness.club.repositories.FitnessClubRepository;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.fitness.club.services.FitnessClubService;
import com.movem.backend.commons.Event.Factory.Fitness.FitnessClubEventFactory;
import com.movem.backend.shared.historyandlogs.featureevents.services.FeatureEventTrackingService;
import com.movem.backend.shared.attachment.services.GcsFileStorageService;
import com.movem.backend.commons.enums.Fitness.ClubPrivacy;
import com.movem.backend.commons.enums.Fitness.FitnessClubRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FitnessClubServiceImpl implements FitnessClubService {
    private final FitnessClubRepository fitnessClubRepository;
    private final FeatureEventTrackingService featureEventTrackingService;
    private final FitnessClubEventFactory fitnessClubEventFactory;
    private final FitnessClubMemberRepository fitnessClubMemberRepository;
    private final AttachmentRepository attachmentRepository;
    private final GcsFileStorageService gcsFileStorageService;
    private final FitnessClubMapper fitnessClubMapper;
    private final CurrentUserService currentUserService;


    @Override
    public FitnessClubResponse createClub(CreateFitnessClubRequest request) {
        User currentUser = currentUserService.getCurrentUser();

        FitnessClub club = new FitnessClub();

        club.setName(request.getName());
        club.setDescription(request.getDescription());
        club.setCreatedBy(currentUser);
        club.setPrivacy(request.getPrivacy());
        club.setJoinToken(generateUniqueJoinToken());

        LocalDateTime now = LocalDateTime.now();

        club.setCreatedAt(now);
        club.setUpdatedAt(now);

        FitnessClub saved = fitnessClubRepository.save(club);

        featureEventTrackingService.handle(fitnessClubEventFactory.clubCreated(saved, currentUser));

        FitnessClubMemberId memberId = new FitnessClubMemberId();

        memberId.setClubId(saved.getId());
        memberId.setUserId(currentUser.getId());

        FitnessClubMember owner = new FitnessClubMember();

        owner.setId(memberId);
        owner.setFitnessClub(saved);
        owner.setUser(currentUser);
        owner.setRole(FitnessClubRole.OWNER);
        owner.setJoinedAt(LocalDateTime.now());

        fitnessClubMemberRepository.save(owner);


        return fitnessClubMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public FitnessClubResponse getClub(Integer clubId) {
        FitnessClub club = fitnessClubRepository.findById(clubId).orElseThrow(() -> new ResourceNotFoundException("Fitness club not found."));
        return fitnessClubMapper.toResponse(club);
    }

    @Override
    public FitnessClubResponse getClubByJoinToken(String joinToken) {
        FitnessClub club = fitnessClubRepository.findByJoinToken(joinToken).orElseThrow(() -> new ResourceNotFoundException("Fitness club not found."));
        return fitnessClubMapper.toResponse(club);
    }


    @Override
    @Transactional
    public List<FitnessClubResponse> getMyClubs() {
        User currentUser = currentUserService.getCurrentUser();
        return fitnessClubRepository
                .findByCreatedBy(currentUser)
                .stream()
                .map(fitnessClubMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional
    public List<FitnessClubResponse> getPublicClubs() {
        return fitnessClubRepository
                .findByPrivacy(ClubPrivacy.PUBLIC)
                .stream()
                .map(fitnessClubMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public List<FitnessClubResponse> searchClubs(String keyword) {
        if (keyword == null || keyword.trim().length() < 2) {
            return List.of();
        }

        String search = keyword.trim();

        return fitnessClubRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search)
                .stream()
                .map(fitnessClubMapper::toResponse)
                .toList();
    }

    @Override
    public FitnessClubResponse updateClub(Integer clubId, UpdateFitnessClubRequest request) {
        User currentUser = currentUserService.getCurrentUser();

        FitnessClub club = fitnessClubRepository.findById(clubId).orElseThrow(() -> new ResourceNotFoundException("Fitness club not found."));

        if (!club.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You can only update a club that you created.");
        }

        club.setName(request.getName());
        club.setDescription(request.getDescription());
        club.setPrivacy(request.getPrivacy());
        club.setUpdatedAt(LocalDateTime.now());

        FitnessClub saved = fitnessClubRepository.save(club);
        String oldName = club.getName();
        featureEventTrackingService.handle(fitnessClubEventFactory.clubUpdated(saved, currentUser, oldName) );

        return fitnessClubMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteClub(Integer clubId) {
        User currentUser = currentUserService.getCurrentUser();

        FitnessClub club = fitnessClubRepository.findById(clubId).orElseThrow(() -> new ResourceNotFoundException("Fitness club not found."));

        if (!club.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You can only delete a club that you created.");
        }

        List<Attachment> attachments = attachmentRepository.findByFitnessClub(club);

        for (Attachment attachment : attachments) {
            gcsFileStorageService.delete(attachment.getFilePath());
        }

        attachmentRepository.deleteAll(attachments);

        String oldName = club.getName();

        fitnessClubRepository.delete(club);
        featureEventTrackingService.handle(fitnessClubEventFactory.clubDeleted(club, currentUser));
    }

    private String generateUniqueJoinToken() {
        String token;
        do {token = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 12)
                    .toUpperCase();

        } while (fitnessClubRepository.existsByJoinToken(token));
        return token;
    }
}