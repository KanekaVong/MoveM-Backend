package com.movem.backend.Entity.Trip;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trip_budgets", indexes = {
        @Index(name = "idx_trip_budget_trip", columnList = "trip_activity_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_activity_id", nullable = false)
    private Trip trip;

    @Column(nullable = false)
    private String category;

    @Column(name = "allocated_amount", nullable = false)
    private BigDecimal allocatedAmount;

    @Column(name = "spent_amount")
    private BigDecimal spentAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripExpense> expenses = new ArrayList<>();
}
