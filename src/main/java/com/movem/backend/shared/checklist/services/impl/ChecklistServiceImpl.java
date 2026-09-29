package com.movem.backend.shared.checklist.services.impl;

import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.task.dtos.requests.Create.CreateChecklistItemRequest;
import com.movem.backend.shared.checklist.dtos.requests.UpdateChecklistItemRequest;
import com.movem.backend.shared.checklist.dtos.responses.ChecklistResponse;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.task.entities.Task;
import com.movem.backend.shared.checklist.entities.Checklist;
import com.movem.backend.trip.entities.Trip;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.shared.checklist.mapper.ChecklistMapper;
import com.movem.backend.shared.checklist.repository.ChecklistRepository;
import com.movem.backend.task.repositories.TaskRepository;
import com.movem.backend.trip.repositories.TripRepository;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.commons.Event.Factory.ChecklistEventFactory;
import com.movem.backend.shared.historyandlogs.featureevents.services.FeatureEventTrackingService;
import com.movem.backend.shared.checklist.services.ChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChecklistServiceImpl implements ChecklistService {
    private final TaskRepository taskRepository;
    private final TripRepository tripRepository;
    private final ChecklistRepository checklistRepository;
    private final CurrentUserService currentUserService;
    private final ChecklistMapper checklistMapper;
    private final FeatureEventTrackingService featureEventTrackingService;
    private final ChecklistEventFactory checklistEventFactory;

    @Override
    public List<ChecklistResponse> getChecklistItems(String activityId) {
        Optional<Task> task = taskRepository.findByActivityId(activityId);

        if (task.isPresent()) {
            return checklistRepository.findByTaskOrderByIdAsc(task.get()).stream().map(checklistMapper::toResponse).toList();
        }

        Trip trip = tripRepository.findByActivityId(activityId).orElseThrow(() -> new ResourceNotFoundException("Activity not found."));

        return checklistRepository
                .findByTripOrderByIdAsc(trip)
                .stream()
                .map(checklistMapper::toResponse)
                .toList();
    }

    @Override
    public void addChecklistItem(String activityId, CreateChecklistItemRequest request) {
        Optional<Task> task = taskRepository.findByActivityId(activityId);

        if (task.isPresent()) {

            Checklist checklist = new Checklist();

            checklist.setTask(task.get());
            checklist.setItemName(request.getItemName());
            checklist.setIsCompleted(false);
            checklist.setCreatedAt(LocalDateTime.now());

            Checklist saved = checklistRepository.save(checklist);

            User currentUser = currentUserService.getCurrentUser();

            featureEventTrackingService.handle(checklistEventFactory.added(task.get().getActivity(), currentUser, 1));

            return;
        }

        Trip trip = tripRepository.findByActivityId(activityId).orElseThrow(() -> new ResourceNotFoundException("Activity not found."));

        Checklist checklist = new Checklist();

        checklist.setTrip(trip);
        checklist.setItemName(request.getItemName());
        checklist.setIsCompleted(false);
        checklist.setCreatedAt(LocalDateTime.now());

        Checklist saved = checklistRepository.save(checklist);

        User currentUser = currentUserService.getCurrentUser();

        featureEventTrackingService.handle(checklistEventFactory.added(trip.getActivity(), currentUser, 1));
    }

    @Override
    public void updateChecklistItem(String activityId, Integer checklistId, UpdateChecklistItemRequest request) {
        Checklist checklist = checklistRepository.findById(checklistId).orElseThrow(() -> new ResourceNotFoundException("Checklist item not found."));

        Activity activity = null;

        if (checklist.getTask() != null) {
            if (!checklist.getTask()
                    .getActivityId()
                    .equals(activityId)) {

                throw new IllegalArgumentException("Checklist item does not belong to this activity.");
            }

            activity = checklist.getTask().getActivity();

        } else if (checklist.getTrip() != null) {
            if (!checklist.getTrip()
                    .getActivityId()
                    .equals(activityId)) {

                throw new IllegalArgumentException("Checklist item does not belong to this activity.");
            }
            activity = checklist.getTrip().getActivity();

        } else {
            throw new ResourceNotFoundException("Checklist activity not found.");
        }

        String oldItemName = checklist.getItemName();

        checklist.setItemName(request.getItemName());

        if (request.getIsCompleted() != null) {
            checklist.setIsCompleted(request.getIsCompleted());
        }

        Checklist saved = checklistRepository.save(checklist);

        User currentUser = currentUserService.getCurrentUser();

        featureEventTrackingService.handle(checklistEventFactory.updated(saved, currentUser, oldItemName));
    }

    @Override
    public void toggleChecklistCompletion(String activityId, Integer checklistId) {
        Checklist checklist = checklistRepository.findById(checklistId).orElseThrow(() -> new ResourceNotFoundException("Checklist item not found."));

        if (checklist.getTask() != null) {

            if (!checklist.getTask().getActivityId().equals(activityId)) {
                throw new IllegalArgumentException("Checklist item does not belong to this activity.");
            }

        } else if (checklist.getTrip() != null) {
            if (!checklist.getTrip().getActivityId().equals(activityId)) {
                throw new IllegalArgumentException("Checklist item does not belong to this activity.");
            }

        } else {
            throw new ResourceNotFoundException("Checklist activity not found.");
        }

        boolean oldCompleted = Boolean.TRUE.equals(checklist.getIsCompleted());
        boolean newCompleted = !oldCompleted;

        checklist.setIsCompleted(newCompleted);
        Checklist saved = checklistRepository.save(checklist);

        User currentUser = currentUserService.getCurrentUser();

        featureEventTrackingService.handle(checklistEventFactory.toggled(saved, currentUser, oldCompleted, newCompleted));
    }

    @Override
    public void deleteChecklistItem(String activityId, Integer checklistId) {
        Checklist checklist = checklistRepository.findById(checklistId).orElseThrow(() -> new ResourceNotFoundException("Checklist item not found."));

        if (checklist.getTask() != null) {

            if (!checklist.getTask().getActivityId().equals(activityId)) {
                throw new IllegalArgumentException("Checklist item does not belong to this activity.");
            }

        } else if (checklist.getTrip() != null) {
            if (!checklist.getTrip().getActivityId().equals(activityId)) {
                throw new IllegalArgumentException("Checklist item does not belong to this activity.");
            }
        } else {
            throw new ResourceNotFoundException("Checklist activity not found.");
        }

        checklistRepository.delete(checklist);
        User currentUser = currentUserService.getCurrentUser();

        featureEventTrackingService.handle(checklistEventFactory.removed(checklist, currentUser));
    }
}