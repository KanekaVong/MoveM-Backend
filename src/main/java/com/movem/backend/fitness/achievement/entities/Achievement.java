package com.movem.backend.fitness.achievement.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "achievements")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(nullable = false, length = 100)
    String name;
    @Column(length = 255)
    String description;
    @Column(length = 255)
    String icon;
    @Column(nullable = false, length = 30)
    String category;
    @Column(name = "condition_type", nullable = false, length = 50)
    String conditionType;
    @Column(name = "condition_value", nullable = false, precision = 12, scale = 2)
    BigDecimal conditionValue;
    @Column(name = "created_at")
    LocalDateTime createdAt;
}