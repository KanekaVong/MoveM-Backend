package com.movem.backend.shared.attachment.repositories;

import com.movem.backend.shared.attachment.entities.Attachment;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.fitness.challenges.entities.GroupFitnessChallenge;
import com.movem.backend.fitness.club.entities.FitnessClub;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutSession;
import com.movem.backend.task.entities.Task;
import com.movem.backend.trip.entities.Trip;
import com.movem.backend.commons.enums.Fitness.FitnessAttachmentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByUploadedByAndDeletedAtIsNull(User user);
    Optional<Attachment> findByIdAndUploadedByAndDeletedAtIsNull(Long id, User user);
    List<Attachment> findByTaskAndDeletedAtIsNull(Task task);
    List<Attachment> findByTripAndDeletedAtIsNull(Trip trip);
    List<Attachment> findByTripActivityIdAndDeletedAtIsNull(String activityId);
    List<Attachment> findByWorkoutSessionAndDeletedAtIsNull(FitnessWorkoutSession workoutSession);
    List<Attachment> findByFitnessClubAndDeletedAtIsNull(FitnessClub fitnessClub);
    List<Attachment> findByFitnessClub(FitnessClub fitnessClub);
    List<Attachment> findByGroupFitnessChallengeAndDeletedAtIsNull(GroupFitnessChallenge groupFitnessChallenge);
    List<Attachment> findByFitnessClubAndAttachmentTypeAndDeletedAtIsNull(FitnessClub fitnessClub, FitnessAttachmentType attachmentType);
    List<Attachment> findByGroupFitnessChallengeAndAttachmentTypeAndDeletedAtIsNull(GroupFitnessChallenge groupFitnessChallenge, FitnessAttachmentType attachmentType);

    List<Attachment> findByTripAndDeletedAtIsNullAndIdNot(Trip trip, Long id);
}