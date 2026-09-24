package com.movem.backend.fitness.profileandgoal.entities;

import com.movem.backend.authentication.entities.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_profile")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessProfile {
    @Id
    @Column(name = "user_id")
    Integer userId;
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    User user;
    @Column(name = "height", precision = 5, scale = 2)
    BigDecimal height;
    @Column(name = "weight", precision = 5, scale = 2)
    BigDecimal weight;
    @Column(name = "bmi", precision = 4, scale = 2)
    BigDecimal bmi;
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}
