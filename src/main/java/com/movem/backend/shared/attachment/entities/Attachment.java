package com.movem.backend.shared.attachment.entities;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.fitness.challenges.entities.GroupFitnessChallenge;
import com.movem.backend.fitness.club.entities.FitnessClub;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutSession;
import com.movem.backend.task.entities.Task;
import com.movem.backend.trip.entities.Trip;
import com.movem.backend.commons.enums.Fitness.FitnessAttachmentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "attachment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_file_name", nullable = false)
    private String originalFileName;

    @Column(name = "stored_file_name", nullable = false, unique = true)
    private String storedFileName;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_activity_id")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_activity_id")
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fitness_club_id")
    private FitnessClub fitnessClub;

    @Enumerated(EnumType.STRING)
    @Column(name = "attachment_type", length = 30)
    private FitnessAttachmentType attachmentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_challenge_id")
    private GroupFitnessChallenge groupFitnessChallenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_session_id")
    private FitnessWorkoutSession workoutSession;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}