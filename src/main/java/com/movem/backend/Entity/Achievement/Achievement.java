package com.movem.backend.Entity.Achievement;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "achievements")
@Getter
@Setter
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(length = 255)
    private String icon;

    @Column(nullable = false, length = 30)
    private String category;

    @Column(name = "condition_type", nullable = false, length = 50)
    private String conditionType;

    @Column(name = "condition_value", nullable = false, precision = 12, scale = 2)
    private BigDecimal conditionValue;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}