package com.movem.backend.Entity.Trip;

import com.movem.backend.Entity.Auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trip_expenses", indexes = {
        @Index(name = "idx_trip_expense_budget", columnList = "budget_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    private TripBudget budget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payer_id", nullable = false)
    private User payer;

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;

    @Column(name = "expense_date")
    private LocalDateTime expenseDate;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripExpenseSplit> splits = new ArrayList<>();
}
