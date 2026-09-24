package com.movem.backend.trip.repositories;

import com.movem.backend.trip.entities.TripBudget;
import com.movem.backend.trip.entities.TripExpense;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripExpenseRepository extends JpaRepository<TripExpense, Integer> {

    @EntityGraph(attributePaths = {"payer", "splits", "splits.user", "budget"})
    List<TripExpense> findByBudgetIn(List<TripBudget> budgets);

    @EntityGraph(attributePaths = {"payer", "splits", "splits.user", "budget"})
    List<TripExpense> findByBudget(TripBudget budget);
}
