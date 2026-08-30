package com.movem.backend.Service.Implement.TripServices;

import com.movem.backend.Dto.request.TripRequest.Create.CreateTripBudgetRequest;
import com.movem.backend.Dto.request.TripRequest.Create.CreateTripExpenseRequest;
import com.movem.backend.Dto.request.TripRequest.Update.UpdateTripBudgetRequest;
import com.movem.backend.Dto.response.TripResponses.TripBudgetResponse;
import com.movem.backend.Dto.response.TripResponses.TripExpenseResponse;
import com.movem.backend.Dto.response.TripResponses.TripExpenseSplitResponse;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Collaboration.ActivityGroup;
import com.movem.backend.Entity.Trip.Trip;
import com.movem.backend.Entity.Trip.TripBudget;
import com.movem.backend.Entity.Trip.TripExpense;
import com.movem.backend.Entity.Trip.TripExpenseSplit;
import com.movem.backend.Exception.BadRequestException;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Exception.UnauthorizedActionException;
import com.movem.backend.Repository.CollaborationRepository.GroupRepository;
import com.movem.backend.Repository.SharedRepository.GroupMemberRepository;
import com.movem.backend.Repository.TripRepositories.TripBudgetRepository;
import com.movem.backend.Repository.TripRepositories.TripExpenseRepository;
import com.movem.backend.Repository.TripRepositories.TripRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.Event.Factory.Trip.TripEventFactory;
import com.movem.backend.Service.Event.FeatureEventTrackingService;
import com.movem.backend.Service.SharedServices.ActivityPermissionService;
import com.movem.backend.Service.TripServices.TripBudgetService;
import com.movem.backend.model.enums.Trip.TripSplitMode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TripBudgetServiceImpl implements TripBudgetService {

    private final TripRepository tripRepository;
    private final TripBudgetRepository tripBudgetRepository;
    private final TripExpenseRepository tripExpenseRepository;

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    private final ActivityPermissionService activityPermissionService;
    private final CurrentUserService currentUserService;

    private final FeatureEventTrackingService featureEventTrackingService;
    private final TripEventFactory tripEventFactory;

    @Override
    public TripBudgetResponse addBudgetCategory(
            String tripActivityId,
            CreateTripBudgetRequest request
    ) {

        User user = currentUserService.getCurrentUser();

        Trip trip = findTripOrThrow(tripActivityId);

        activityPermissionService.validateCanManageTrip(
                trip.getActivity(),
                user
        );

        TripBudget budget = new TripBudget();

        budget.setTrip(trip);
        budget.setCategory(request.getCategory());
        budget.setAllocatedAmount(request.getAllocatedAmount());
        budget.setSpentAmount(BigDecimal.ZERO);

        tripBudgetRepository.save(budget);

        return toResponse(budget, trip);
    }


    @Override
    public List<TripBudgetResponse> getBudgets(
            String tripActivityId
    ) {

        User user = currentUserService.getCurrentUser();

        Trip trip = findTripOrThrow(tripActivityId);

        activityPermissionService.validateActivityAccess(
                trip.getActivity(),
                user
        );

        return tripBudgetRepository.findByTrip(trip)
                .stream()
                .map(budget -> toResponse(budget, trip))
                .collect(Collectors.toList());
    }


    @Override
    public TripBudgetResponse updateBudgetCategory(
            String tripActivityId,
            Integer budgetId,
            UpdateTripBudgetRequest request
    ) {

        User user = currentUserService.getCurrentUser();

        Trip trip = findTripOrThrow(tripActivityId);

        activityPermissionService.validateCanManageTrip(
                trip.getActivity(),
                user
        );

        TripBudget budget =
                findBudgetOrThrow(trip, budgetId);

        budget.setCategory(request.getCategory());
        budget.setAllocatedAmount(
                request.getAllocatedAmount()
        );

        return toResponse(budget, trip);
    }


    @Override
    public void deleteBudgetCategory(
            String tripActivityId,
            Integer budgetId
    ) {

        User user = currentUserService.getCurrentUser();

        Trip trip = findTripOrThrow(tripActivityId);

        activityPermissionService.validateCanManageTrip(
                trip.getActivity(),
                user
        );

        TripBudget budget =
                findBudgetOrThrow(trip, budgetId);

        tripBudgetRepository.delete(budget);
    }

    @Override
    public TripExpenseResponse logExpense(
            String tripActivityId,
            CreateTripExpenseRequest request
    ) {

        User user = currentUserService.getCurrentUser();

        Trip trip = findTripOrThrow(tripActivityId);

        activityPermissionService.validateCanContributeToTrip(
                trip.getActivity(),
                user
        );

        TripBudget budget =
                findBudgetOrThrow(
                        trip,
                        request.getBudgetId()
                );

        User payer = user;

        if (
                request.getPayerId() != null
                        && !request.getPayerId().equals(user.getId())
        ) {

            payer = resolveMember(
                    trip,
                    request.getPayerId()
            );
        }

        TripExpense expense = new TripExpense();

        expense.setBudget(budget);
        expense.setPayer(payer);
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());

        expense.setExpenseDate(
                request.getExpenseDate() != null
                        ? request.getExpenseDate()
                        : LocalDateTime.now()
        );

        expense.setSplits(
                buildSplits(
                        trip,
                        expense,
                        request
                )
        );

        BigDecimal currentSpent =
                budget.getSpentAmount() == null
                        ? BigDecimal.ZERO
                        : budget.getSpentAmount();

        budget.setSpentAmount(
                currentSpent.add(request.getAmount())
        );

        tripExpenseRepository.save(expense);

        featureEventTrackingService.handle(
                tripEventFactory.expenseLogged(
                        expense,
                        user
                )
        );


        return toResponse(
                expense,
                budget
        );
    }


    @Override
    public List<TripExpenseResponse> getExpenses(
            String tripActivityId,
            Integer budgetId
    ) {

        User user = currentUserService.getCurrentUser();

        Trip trip = findTripOrThrow(tripActivityId);

        activityPermissionService.validateActivityAccess(
                trip.getActivity(),
                user
        );


        // Get expenses for one budget
        if (budgetId != null) {

            TripBudget budget =
                    findBudgetOrThrow(
                            trip,
                            budgetId
                    );

            return tripExpenseRepository
                    .findByBudget(budget)
                    .stream()
                    .map(expense ->
                            toResponse(
                                    expense,
                                    budget
                            )
                    )
                    .collect(Collectors.toList());
        }


        // Get expenses for the entire trip
        List<TripBudget> budgets =
                tripBudgetRepository.findByTrip(trip);

        return tripExpenseRepository
                .findByBudgetIn(budgets)
                .stream()
                .map(expense ->
                        toResponse(
                                expense,
                                expense.getBudget()
                        )
                )
                .collect(Collectors.toList());
    }


    @Override
    public void deleteExpense(
            String tripActivityId,
            Integer expenseId
    ) {

        User user = currentUserService.getCurrentUser();

        Trip trip = findTripOrThrow(tripActivityId);

        activityPermissionService.validateCanManageTrip(
                trip.getActivity(),
                user
        );

        TripExpense expense =
                findExpenseOrThrow(
                        trip,
                        expenseId
                );

        TripBudget budget =
                expense.getBudget();


        BigDecimal currentSpent =
                budget.getSpentAmount() == null
                        ? BigDecimal.ZERO
                        : budget.getSpentAmount();

        budget.setSpentAmount(
                currentSpent
                        .subtract(expense.getAmount())
                        .max(BigDecimal.ZERO)
        );

        tripExpenseRepository.delete(expense);
    }

    @Override
    public TripExpenseResponse settleSplit(
            String tripActivityId,
            Integer expenseId,
            Integer splitId
    ) {

        User user = currentUserService.getCurrentUser();

        Trip trip = findTripOrThrow(tripActivityId);

        activityPermissionService.validateCanEditActivity(
                trip.getActivity(),
                user
        );

        TripExpense expense =
                findExpenseOrThrow(
                        trip,
                        expenseId
                );

        TripExpenseSplit split =
                expense.getSplits()
                        .stream()
                        .filter(s ->
                                s.getId().equals(splitId)
                        )
                        .findFirst()
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Split not found: " + splitId
                                )
                        );

        if (!split.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedActionException(
                    "You can only settle your own split."
            );
        }

        if (Boolean.TRUE.equals(split.getIsSettled())) {
            throw new BadRequestException(
                    "This split is already settled."
            );
        }

        split.setIsSettled(true);
        split.setSettledAt(LocalDateTime.now());


        return toResponse(
                expense,
                expense.getBudget()
        );
    }

    private List<TripExpenseSplit> buildSplits(
            Trip trip,
            TripExpense expense,
            CreateTripExpenseRequest request
    ) {

        if (request.getSplitMode() == TripSplitMode.NONE) {
            return new ArrayList<>();
        }

        if (request.getSplitMode() == TripSplitMode.CUSTOM) {

            if (
                    request.getCustomSplits() == null
                            || request.getCustomSplits().isEmpty()
            ) {

                throw new BadRequestException(
                        "customSplits is required when splitMode is CUSTOM"
                );
            }


            BigDecimal sum =
                    request.getCustomSplits()
                            .stream()
                            .map(
                                    CreateTripExpenseRequest
                                            .ExpenseSplitEntry::getAmount
                            )
                            .reduce(
                                    BigDecimal.ZERO,
                                    BigDecimal::add
                            );


            if (
                    sum.compareTo(
                            request.getAmount()
                    ) != 0
            ) {

                throw new BadRequestException(
                        "customSplits must add up to the expense amount"
                );
            }


            return request.getCustomSplits()
                    .stream()
                    .map(entry -> {

                        TripExpenseSplit split =
                                new TripExpenseSplit();

                        split.setExpense(expense);

                        split.setUser(
                                resolveMember(
                                        trip,
                                        entry.getUserId()
                                )
                        );

                        split.setAmountOwed(
                                entry.getAmount()
                        );

                        split.setIsSettled(false);

                        return split;
                    })
                    .collect(Collectors.toList());
        }

        List<User> members =
                tripMembers(trip);

        int count = members.size();

        if (count == 0) {

            throw new BadRequestException(
                    "Trip has no members to split the expense with"
            );
        }


        BigDecimal share =
                request.getAmount()
                        .divide(
                                BigDecimal.valueOf(count),
                                2,
                                RoundingMode.HALF_UP
                        );


        BigDecimal roundingRemainder =
                request.getAmount()
                        .subtract(
                                share.multiply(
                                        BigDecimal.valueOf(count)
                                )
                        );


        List<TripExpenseSplit> splits =
                new ArrayList<>();


        for (int i = 0; i < count; i++) {

            TripExpenseSplit split =
                    new TripExpenseSplit();

            split.setExpense(expense);

            split.setUser(
                    members.get(i)
            );

            split.setAmountOwed(
                    i == 0
                            ? share.add(
                            roundingRemainder
                    )
                            : share
            );

            split.setIsSettled(false);

            splits.add(split);
        }

        return splits;
    }

    private List<User> tripMembers(
            Trip trip
    ) {

        Optional<ActivityGroup> group =
                groupRepository.findByActivity(
                        trip.getActivity()
                );

        List<User> members =
                new ArrayList<>();


        // Trip owner
        members.add(
                trip.getActivity().getUser()
        );


        // Collaborative members
        group.ifPresent(g ->
                groupMemberRepository
                        .findByActivityGroup(g)
                        .forEach(groupMember -> {

                            User member =
                                    groupMember.getUser();

                            if (
                                    !member.getId().equals(
                                            trip.getActivity()
                                                    .getUser()
                                                    .getId()
                                    )
                            ) {

                                members.add(member);
                            }
                        })
        );

        return members;
    }


    private User resolveMember(
            Trip trip,
            Integer userId
    ) {

        return tripMembers(trip)
                .stream()
                .filter(user ->
                        user.getId().equals(userId)
                )
                .findFirst()
                .orElseThrow(() ->
                        new BadRequestException(
                                "User "
                                        + userId
                                        + " is not part of this trip"
                        )
                );
    }

    private TripBudgetResponse toResponse(
            TripBudget budget,
            Trip trip
    ) {

        BigDecimal spent =
                budget.getSpentAmount() == null
                        ? BigDecimal.ZERO
                        : budget.getSpentAmount();


        BigDecimal remaining =
                budget.getAllocatedAmount()
                        .subtract(spent);


        int memberCount =
                tripMembers(trip).size();


        BigDecimal perPerson =
                memberCount > 0
                        ? budget.getAllocatedAmount()
                        .divide(
                                BigDecimal.valueOf(
                                        memberCount
                                ),
                                2,
                                RoundingMode.HALF_UP
                        )
                        : budget.getAllocatedAmount();


        return TripBudgetResponse.builder()
                .id(budget.getId())
                .category(budget.getCategory())
                .allocatedAmount(
                        budget.getAllocatedAmount()
                )
                .spentAmount(spent)
                .remaining(remaining)
                .perPersonShare(perPerson)
                .build();
    }


    private TripExpenseResponse toResponse(
            TripExpense expense,
            TripBudget budget
    ) {

        return TripExpenseResponse.builder()
                .id(expense.getId())
                .budgetId(budget.getId())
                .category(budget.getCategory())
                .payerId(
                        expense.getPayer().getId()
                )
                .payerName(
                        expense.getPayer().getUsername()
                )
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .expenseDate(expense.getExpenseDate())
                .splits(
                        expense.getSplits()
                                .stream()
                                .map(this::toResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }


    private TripExpenseSplitResponse toResponse(
            TripExpenseSplit split
    ) {

        return TripExpenseSplitResponse.builder()
                .id(split.getId())
                .userId(split.getUser().getId())
                .username(split.getUser().getUsername())
                .amountOwed(split.getAmountOwed())
                .isSettled(split.getIsSettled())
                .settledAt(split.getSettledAt())
                .build();
    }

    private Trip findTripOrThrow(
            String tripActivityId
    ) {

        return tripRepository
                .findByActivityId(tripActivityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Trip not found: "
                                        + tripActivityId
                        )
                );
    }


    private TripBudget findBudgetOrThrow(
            Trip trip,
            Integer budgetId
    ) {

        return tripBudgetRepository
                .findByIdAndTrip(
                        budgetId,
                        trip
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Budget category not found: "
                                        + budgetId
                        )
                );
    }


    private TripExpense findExpenseOrThrow(
            Trip trip,
            Integer expenseId
    ) {

        return tripExpenseRepository
                .findById(expenseId)
                .filter(expense ->
                        expense.getBudget()
                                .getTrip()
                                .getActivityId()
                                .equals(
                                        trip.getActivityId()
                                )
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Expense not found: "
                                        + expenseId
                        )
                );
    }
}