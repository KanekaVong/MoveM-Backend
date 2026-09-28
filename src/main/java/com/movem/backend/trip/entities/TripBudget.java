package com.movem.backend.trip.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trip_budgets", indexes = {@Index(name = "idx_trip_budget_trip", columnList = "trip_activity_id")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripBudget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_activity_id", nullable = false)
    Trip trip;

    @Column(nullable = false)
    String category;

    @Column(name = "allocated_amount", nullable = false)
    BigDecimal allocatedAmount;

    @Column(name = "spent_amount")
    BigDecimal spentAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    List<TripExpense> expenses = new ArrayList<>();
}
