package com.movem.backend.trip.services;


import com.movem.backend.trip.dtos.requests.Create.CreateTripBudgetRequest;
import com.movem.backend.trip.dtos.requests.Create.CreateTripExpenseRequest;
import com.movem.backend.trip.dtos.requests.Update.UpdateTripBudgetRequest;
import com.movem.backend.trip.dtos.responses.TripBudgetResponse;
import com.movem.backend.trip.dtos.responses.TripExpenseResponse;

import java.math.BigDecimal;
import java.util.List;

public interface TripBudgetService {
    TripBudgetResponse addBudgetCategory(String tripActivityId, CreateTripBudgetRequest request);
    List<TripBudgetResponse> getBudgets(String tripActivityId);
    TripBudgetResponse updateBudgetCategory(String tripActivityId, Integer budgetId, UpdateTripBudgetRequest request);
    void deleteBudgetCategory(String tripActivityId, Integer budgetId);
    TripExpenseResponse logExpense(String tripActivityId, CreateTripExpenseRequest request);
    List<TripExpenseResponse> getExpenses(String tripActivityId, Integer budgetId);
    void deleteExpense(String tripActivityId, Integer expenseId);
    TripExpenseResponse settleSplit(String tripActivityId, Integer expenseId, Integer splitId);
    void updateTotalBudget(String tripActivityId, BigDecimal totalBudget);
}
