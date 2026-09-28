package com.movem.backend.fitness.club.entities;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.Fitness.ClubPrivacy;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_clubs", indexes = {@Index(name = "idx_fitness_club_creator", columnList = "created_by"), @Index(name = "idx_fitness_club_privacy", columnList = "privacy"), @Index(name = "idx_fitness_club_token", columnList = "join_token")})
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessClub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(nullable = false, length = 150)
    String name;
    @Column(columnDefinition = "TEXT")
    String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    User createdBy;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    ClubPrivacy privacy;
    @Column(name = "join_token", unique = true, length = 100)
    String joinToken;
    @Column(name = "created_at")
    LocalDateTime createdAt;
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}