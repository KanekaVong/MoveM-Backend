package com.movem.backend.fitness.club.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessClubMemberId implements Serializable {
    @Column(name = "club_id")
    Integer clubId;
    @Column(name = "user_id")
    Integer userId;
}