package com.movem.backend.Repository.AttachmentRepository;

import com.movem.backend.Entity.Attachment.Attachment;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutSession;
import com.movem.backend.Entity.Tasks.Task;
import com.movem.backend.Entity.Trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository
        extends JpaRepository<Attachment, Long> {

    List<Attachment> findByUploadedByAndDeletedAtIsNull(User user);

    Optional<Attachment> findByIdAndUploadedByAndDeletedAtIsNull(
            Long id,
            User user
    );

    List<Attachment> findByTaskAndDeletedAtIsNull(
            Task task
    );

    List<Attachment> findByTripAndDeletedAtIsNull(
            Trip trip
    );

    List<Attachment> findByTripActivityIdAndDeletedAtIsNull(
            String activityId
    );

    List<Attachment> findByWorkoutSessionAndDeletedAtIsNull(
            FitnessWorkoutSession workoutSession
    );
}