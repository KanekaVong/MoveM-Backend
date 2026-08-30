package com.movem.backend.Controller.TripControllers;


import com.movem.backend.Dto.request.TripRequest.Create.CreateTripBudgetRequest;
import com.movem.backend.Dto.request.TripRequest.Create.CreateTripExpenseRequest;
import com.movem.backend.Dto.request.TripRequest.Update.UpdateTripBudgetRequest;
import com.movem.backend.Dto.response.TripResponses.TripBudgetResponse;
import com.movem.backend.Dto.response.TripResponses.TripExpenseResponse;
import com.movem.backend.Service.TripServices.TripBudgetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips/{activityId}")
@Tag( name = "Trip - Trip Budget",
        description = "Create trip, add collaborators, plan trips seamlessly")
@RequiredArgsConstructor
public class TripBudgetController {

    private final TripBudgetService tripBudgetService;

    @PostMapping("/budgets")
    public ResponseEntity<TripBudgetResponse> addBudgetCategory(
            @PathVariable String activityId,
            @Valid @RequestBody CreateTripBudgetRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tripBudgetService.addBudgetCategory(activityId, request));
    }

    @GetMapping("/budgets")
    public ResponseEntity<List<TripBudgetResponse>> getBudgets(@PathVariable String activityId) {
        return ResponseEntity.ok(tripBudgetService.getBudgets(activityId));
    }

    @PutMapping("/budgets/{budgetId}")
    public ResponseEntity<TripBudgetResponse> updateBudgetCategory(
            @PathVariable String activityId,
            @PathVariable Integer budgetId,
            @Valid @RequestBody UpdateTripBudgetRequest request
    ) {
        return ResponseEntity.ok(tripBudgetService.updateBudgetCategory(activityId, budgetId, request));
    }

    @DeleteMapping("/budgets/{budgetId}")
    public ResponseEntity<Void> deleteBudgetCategory(
            @PathVariable String activityId,
            @PathVariable Integer budgetId
    ) {
        tripBudgetService.deleteBudgetCategory(activityId, budgetId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/expenses")
    public ResponseEntity<TripExpenseResponse> logExpense(
            @PathVariable String activityId,
            @Valid @RequestBody CreateTripExpenseRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tripBudgetService.logExpense(activityId, request));
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<TripExpenseResponse>> getExpenses(
            @PathVariable String activityId,
            @RequestParam(required = false) Integer budgetId
    ) {
        return ResponseEntity.ok(tripBudgetService.getExpenses(activityId, budgetId));
    }

    @DeleteMapping("/expenses/{expenseId}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable String activityId,
            @PathVariable Integer expenseId
    ) {
        tripBudgetService.deleteExpense(activityId, expenseId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/expenses/{expenseId}/splits/{splitId}/settle")
    public ResponseEntity<TripExpenseResponse> settleSplit(
            @PathVariable String activityId,
            @PathVariable Integer expenseId,
            @PathVariable Integer splitId
    ) {
        return ResponseEntity.ok(tripBudgetService.settleSplit(activityId, expenseId, splitId));
    }
}
