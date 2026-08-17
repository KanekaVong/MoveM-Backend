package com.movem.backend.Entity.Fitness.Club;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class FitnessClubMemberId implements Serializable {

    @Column(name = "club_id")
    private Integer clubId;

    @Column(name = "user_id")
    private Integer userId;
}