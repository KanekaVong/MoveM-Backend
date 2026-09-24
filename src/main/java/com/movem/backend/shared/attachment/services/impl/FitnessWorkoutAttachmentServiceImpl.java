package com.movem.backend.shared.attachment.services.impl;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import com.movem.backend.shared.attachment.entities.Attachment;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.fitness.challenges.entities.GroupFitnessChallenge;
import com.movem.backend.fitness.club.entities.FitnessClub;
import com.movem.backend.fitness.club.entities.FitnessClubMember;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutSession;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.commons.Exception.UnauthorizedActionException;
import com.movem.backend.shared.attachment.repositories.AttachmentRepository;
import com.movem.backend.fitness.challenges.repositories.GroupFitnessChallengeRepository;
import com.movem.backend.fitness.club.repositories.FitnessClubMemberRepository;
import com.movem.backend.fitness.club.repositories.FitnessClubRepository;
import com.movem.backend.fitness.workout.repositories.FitnessWorkoutSessionRepository;
import com.movem.backend.shared.attachment.services.AttachmentService;
import com.movem.backend.shared.attachment.services.FitnessWorkoutAttachmentService;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.commons.enums.Fitness.FitnessClubRole;
import com.movem.backend.commons.enums.Fitness.FitnessWorkoutStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FitnessWorkoutAttachmentServiceImpl implements FitnessWorkoutAttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final FitnessWorkoutSessionRepository workoutSessionRepository;
    private final AttachmentService attachmentService;
    private final CurrentUserService currentUserService;
    private final FitnessClubRepository fitnessClubRepository;
    private final FitnessClubMemberRepository fitnessClubMemberRepository;
    private final GroupFitnessChallengeRepository groupFitnessChallengeRepository;



    @Override
    public AttachmentResponse upload(Integer sessionId, MultipartFile file) {
        User currentUser = currentUserService.getCurrentUser();
        FitnessWorkoutSession session = workoutSessionRepository.findByIdAndUser(sessionId, currentUser).orElseThrow(() -> new ResourceNotFoundException("Workout session not found."));

        if (session.getStatus() != FitnessWorkoutStatus.COMPLETED) {
            throw new IllegalArgumentException("Attachments can only be added to completed workouts.");
        }

        AttachmentResponse uploaded = attachmentService.upload(file);
        Attachment attachment = attachmentRepository.findById(uploaded.getId()).orElseThrow(() -> new ResourceNotFoundException("Uploaded attachment not found."));
        attachment.setWorkoutSession(session);
        attachment.setCreatedAt(attachment.getCreatedAt() != null ? attachment.getCreatedAt() : LocalDateTime.now());
        Attachment saved = attachmentRepository.save(attachment);

        return attachmentService.toResponse(saved);
    }

    @Override
    public List<AttachmentResponse> getAttachments(Integer sessionId) {
        User currentUser = currentUserService.getCurrentUser();

        FitnessWorkoutSession session = workoutSessionRepository
                        .findByIdAndUser(sessionId, currentUser)
                        .orElseThrow(() -> new ResourceNotFoundException("Workout session not found."));
        return attachmentRepository
                .findByWorkoutSessionAndDeletedAtIsNull(session)
                .stream()
                .map(attachmentService::toResponse)
                .toList();
    }
    @Override
    public AttachmentResponse uploadClubProfile(Integer clubId, MultipartFile file) {
        FitnessClub club = getClub(clubId);
        User currentUser = currentUserService.getCurrentUser();

        requireClubOwner(currentUser, club);

        AttachmentResponse uploaded = attachmentService.upload(file);
        Attachment attachment = getAttachment(uploaded.getId());
        attachment.setFitnessClub(club);
        Attachment saved = attachmentRepository.save(attachment);

        return attachmentService.toResponse(saved);
    }

    @Override
    public AttachmentResponse uploadClubCover(Integer clubId, MultipartFile file) {
        FitnessClub club = getClub(clubId);
        User currentUser = currentUserService.getCurrentUser();

        requireClubOwner(currentUser, club);

        AttachmentResponse uploaded = attachmentService.upload(file);
        Attachment attachment = getAttachment(uploaded.getId());
        attachment.setFitnessClub(club);
        Attachment saved = attachmentRepository.save(attachment);

        return attachmentService.toResponse(saved);
    }

    @Override
    public List<AttachmentResponse> getClubAttachments(Integer clubId) {
        FitnessClub club = getClub(clubId);

        return attachmentRepository
                .findByFitnessClubAndDeletedAtIsNull(club)
                .stream()
                .map(attachmentService::toResponse)
                .toList();
    }

    @Override
    public AttachmentResponse uploadChallengeAttachment(Integer challengeId, MultipartFile file) {
        User currentUser = currentUserService.getCurrentUser();

        GroupFitnessChallenge challenge = groupFitnessChallengeRepository.findById(challengeId)
                        .orElseThrow(() -> new ResourceNotFoundException("Group fitness challenge not found."));

        FitnessClubMember membership = fitnessClubMemberRepository.findByFitnessClubAndUser(challenge.getFitnessClub(), currentUser)
                        .orElseThrow(() -> new UnauthorizedActionException("You are not a member of this fitness club."));

        boolean isOwnerOrAdmin = membership.getRole() == FitnessClubRole.OWNER || membership.getRole() == FitnessClubRole.ADMIN;

        boolean isCreator = challenge.getCreatedBy().getId().equals(currentUser.getId());

        if (!isOwnerOrAdmin && !isCreator) {
            throw new UnauthorizedActionException("You do not have permission to add a picture to this challenge.");
        }

        AttachmentResponse uploaded = attachmentService.upload(file);
        Attachment attachment = getAttachment(uploaded.getId());
        attachment.setGroupFitnessChallenge(challenge);
        Attachment saved = attachmentRepository.save(attachment);

        return attachmentService.toResponse(saved);
    }

    @Override
    public List<AttachmentResponse> getChallengeAttachments(Integer challengeId) {
        GroupFitnessChallenge challenge =
                groupFitnessChallengeRepository.findById(challengeId)
                        .orElseThrow(() -> new ResourceNotFoundException("Group fitness challenge not found."));

        return attachmentRepository
                .findByGroupFitnessChallengeAndDeletedAtIsNull(challenge)
                .stream()
                .map(attachmentService::toResponse)
                .toList();
    }

    private FitnessClub getClub(Integer clubId) {
        return fitnessClubRepository.findById(clubId)
                .orElseThrow(() -> new ResourceNotFoundException("Fitness club not found."));
    }

    private Attachment getAttachment(Long attachmentId) {
        return attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Uploaded attachment not found."));
    }

    private void requireClubOwner(User currentUser, FitnessClub club) {
        if (!club.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException("Only the fitness club owner can manage club pictures.");
        }
    }

}