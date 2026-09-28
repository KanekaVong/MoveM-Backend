package com.movem.backend.trip.entities;

import com.movem.backend.authentication.entities.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trip_expense_splits", indexes = {@Index(name = "idx_trip_split_expense", columnList = "expense_id"), @Index(name = "idx_trip_split_user", columnList = "user_id")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripExpenseSplit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id", nullable = false)
    TripExpense expense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "amount_owed", nullable = false)
    BigDecimal amountOwed;

    @Column(name = "is_settled")
    Boolean isSettled = false;

    @Column(name = "settled_at")
    LocalDateTime settledAt;
}
