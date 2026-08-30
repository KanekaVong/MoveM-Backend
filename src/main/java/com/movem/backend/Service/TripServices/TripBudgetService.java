package com.movem.backend.Service.TripServices;


import com.movem.backend.Dto.request.TripRequest.Create.CreateTripBudgetRequest;
import com.movem.backend.Dto.request.TripRequest.Create.CreateTripExpenseRequest;
import com.movem.backend.Dto.request.TripRequest.Update.UpdateTripBudgetRequest;
import com.movem.backend.Dto.response.TripResponses.TripBudgetResponse;
import com.movem.backend.Dto.response.TripResponses.TripExpenseResponse;

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
}
