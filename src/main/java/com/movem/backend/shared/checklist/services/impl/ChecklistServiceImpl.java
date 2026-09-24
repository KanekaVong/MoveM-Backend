package com.movem.backend.shared.checklist.services.impl;

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
    public void createChecklistItems(Task task, List<CreateChecklistItemRequest> items) {
        if (items == null || items.isEmpty()) {
            return;
        }

        List<Checklist> checklists = new ArrayList<>();

        for (CreateChecklistItemRequest item : items) {

            Checklist checklist = new Checklist();

            checklist.setTask(task);
            checklist.setItemName(item.getItemName());
            checklist.setIsCompleted(false);
            checklist.setCreatedAt(LocalDateTime.now());

            checklists.add(checklist);

            task.getChecklists().add(checklist);
        }

        checklistRepository.saveAll(checklists);

        User currentUser = currentUserService.getCurrentUser();

        featureEventTrackingService.handle(checklistEventFactory.added(task.getActivity(), currentUser, checklists.size()));
    }

    @Override
    public List<ChecklistResponse> getChecklistItems(String activityId) {
        Task task = taskRepository
                .findByActivityId(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found."));

        return checklistRepository
                .findByTaskOrderByIdAsc(task)
                .stream()
                .map(checklistMapper::toResponse)
                .toList();
    }

    @Override
    public void createTripChecklistItems(Trip trip, List<CreateChecklistItemRequest> items) {
        if (items == null || items.isEmpty()) {
            return;
        }

        List<Checklist> checklists = new ArrayList<>();

        for (CreateChecklistItemRequest item : items) {

            Checklist checklist = new Checklist();

            checklist.setTrip(trip);
            checklist.setItemName(item.getItemName());
            checklist.setIsCompleted(false);
            checklist.setCreatedAt(LocalDateTime.now());

            checklists.add(checklist);
        }

        checklistRepository.saveAll(checklists);

        User currentUser = currentUserService.getCurrentUser();

        featureEventTrackingService.handle(checklistEventFactory.added(trip.getActivity(), currentUser, checklists.size()));
    }

    @Override
    public void updateTripChecklistItem(
            String activityId,
            Integer checklistId,
            UpdateChecklistItemRequest request) {

        Trip trip = tripRepository
                .findByActivityId(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Trip not found."));

        Checklist checklist = checklistRepository
                .findById(checklistId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Checklist item not found."));

        // ADD DEBUG HERE
        System.out.println("=== TRIP CHECKLIST DEBUG ===");
        System.out.println("Requested activityId: " + activityId);
        System.out.println("Found trip activityId: " + trip.getActivityId());
        System.out.println("Checklist ID: " + checklist.getId());
        System.out.println("Checklist trip: " + checklist.getTrip());

        if (checklist.getTrip() != null) {
            System.out.println(
                    "Checklist trip activityId: "
                            + checklist.getTrip().getActivityId()
            );
        }

        // YOUR EXISTING VALIDATION
        if (checklist.getTrip() == null ||
                !checklist.getTrip()
                        .getActivityId()
                        .equals(trip.getActivityId())) {

            throw new IllegalArgumentException(
                    "Checklist item does not belong to this trip."
            );
        }

        String oldItemName = checklist.getItemName();

        checklist.setItemName(request.getItemName());

        checklist.setIsCompleted(
                Boolean.TRUE.equals(request.getIsCompleted())
        );

        Checklist saved = checklistRepository.save(checklist);

        User currentUser = currentUserService.getCurrentUser();

        featureEventTrackingService.handle(
                checklistEventFactory.updated(
                        saved,
                        currentUser,
                        oldItemName
                )
        );
    }
    @Override
    public List<ChecklistResponse> getTripChecklistItems(String activityId) {
        Trip trip = tripRepository.findByActivityId(activityId).orElseThrow(() -> new ResourceNotFoundException("Trip not found."));
        return checklistRepository
                .findByTripOrderByIdAsc(trip)
                .stream()
                .map(checklistMapper::toResponse)
                .toList();
    }

    @Override
    public void markChecklistCompleted(Integer checklistId) {
        Checklist checklist = checklistRepository
                .findById(checklistId)
                .orElseThrow(() -> new ResourceNotFoundException("Checklist item not found."));

        boolean oldCompleted = Boolean.TRUE.equals(checklist.getIsCompleted());
        if (oldCompleted) {
            return;
        }
        checklist.setIsCompleted(true);
        Checklist saved = checklistRepository.save(checklist);

        User currentUser = currentUserService.getCurrentUser();

        featureEventTrackingService.handle(checklistEventFactory.completed(saved, currentUser));
    }

    @Override
    public void addChecklistItem(String activityId, CreateChecklistItemRequest request) {

        Task task = taskRepository.findByActivityId(activityId).orElseThrow(() -> new ResourceNotFoundException("Task not found."));

        Checklist checklist = new Checklist();

        checklist.setTask(task);
        checklist.setItemName(request.getItemName());
        checklist.setIsCompleted(false);
        checklist.setCreatedAt(LocalDateTime.now());

        Checklist saved = checklistRepository.save(checklist);

        User currentUser = currentUserService.getCurrentUser();

        featureEventTrackingService.handle( checklistEventFactory.added(task.getActivity(), currentUser, 1));
    }

    @Override
    public void updateChecklistItems(Task task, List<UpdateChecklistItemRequest> requests) {
        if (requests == null) {
            return;
        }

        List<Checklist> existingChecklists =
                checklistRepository.findByTaskOrderByIdAsc(task);

        for (Checklist existing : existingChecklists) {

            boolean stillExists = requests.stream()
                    .anyMatch(request ->
                            request.getId() != null &&
                                    request.getId().equals(existing.getId())
                    );

            if (!stillExists) {
                checklistRepository.delete(existing);
            }
        }

        for (UpdateChecklistItemRequest request : requests) {

            if (request.getId() == null) {

                Checklist checklist = new Checklist();

                checklist.setTask(task);
                checklist.setItemName(request.getItemName());
                checklist.setIsCompleted(
                        Boolean.TRUE.equals(request.getIsCompleted())
                );
                checklist.setCreatedAt(LocalDateTime.now());

                checklistRepository.save(checklist);

            } else {

                Checklist checklist = checklistRepository
                        .findById(request.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Checklist item not found."
                                )
                        );

                if (checklist.getTask() == null ||
                        !checklist.getTask()
                                .getActivityId()
                                .equals(task.getActivityId())) {

                    throw new IllegalArgumentException(
                            "Checklist item does not belong to this task."
                    );
                }

                checklist.setItemName(request.getItemName());
                checklist.setIsCompleted(Boolean.TRUE.equals(request.getIsCompleted()));

                checklistRepository.save(checklist);
            }
        }
    }

    @Override
    public void toggleChecklistCompletion(Integer checklistId) {

        Checklist checklist = checklistRepository.findById(checklistId).orElseThrow(() -> new ResourceNotFoundException("Checklist item not found."));

        boolean oldCompleted = Boolean.TRUE.equals(checklist.getIsCompleted());
        boolean newCompleted = !oldCompleted;

        checklist.setIsCompleted(newCompleted);
        Checklist saved = checklistRepository.save(checklist);

        User currentUser = currentUserService.getCurrentUser();

        featureEventTrackingService.handle(checklistEventFactory.toggled(saved, currentUser, oldCompleted, newCompleted));
    }

    @Override
    public void toggleTripChecklistCompletion(String activityId, Integer checklistId) {

        Trip trip = tripRepository
                .findByActivityId(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Trip not found."
                        ));

        Checklist checklist = checklistRepository
                .findById(checklistId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Checklist item not found."
                        ));

        if (checklist.getTrip() == null ||
                !checklist.getTrip()
                        .getActivityId()
                        .equals(trip.getActivityId())) {

            throw new IllegalArgumentException(
                    "Checklist item does not belong to this trip."
            );
        }

        boolean oldCompleted =
                Boolean.TRUE.equals(checklist.getIsCompleted());

        boolean newCompleted = !oldCompleted;

        checklist.setIsCompleted(newCompleted);

        Checklist saved = checklistRepository.save(checklist);

        User currentUser = currentUserService.getCurrentUser();

        featureEventTrackingService.handle(
                checklistEventFactory.toggled(
                        saved,
                        currentUser,
                        oldCompleted,
                        newCompleted
                )
        );
    }

    @Override
    public void deleteChecklistItem(Integer checklistId) {

        Checklist checklist = checklistRepository.findById(checklistId).orElseThrow(() -> new ResourceNotFoundException("Checklist item not found."));
        checklistRepository.delete(checklist);

        User currentUser = currentUserService.getCurrentUser();

        featureEventTrackingService.handle(checklistEventFactory.removed(checklist, currentUser));
    }
}