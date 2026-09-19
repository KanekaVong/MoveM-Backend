package com.movem.backend.Service.Implement.TaskServices;

import com.movem.backend.Dto.request.TaskRequests.Create.CreateChecklistItemRequest;
import com.movem.backend.Dto.request.TaskRequests.Update.UpdateChecklistItemRequest;
import com.movem.backend.Dto.response.TaskResponses.ChecklistResponse;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Tasks.Task;
import com.movem.backend.Entity.Tasks.Checklist;
import com.movem.backend.Entity.Trip.Trip;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.TaskMapper.TaskChecklistMapper;
import com.movem.backend.Repository.TaskRepositories.ChecklistRepository;
import com.movem.backend.Repository.TaskRepositories.TaskRepository;
import com.movem.backend.Repository.TripRepositories.TripRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.Event.Factory.ChecklistEventFactory;
import com.movem.backend.Service.Event.FeatureEventTrackingService;
import com.movem.backend.Service.TaskServices.ChecklistService;
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
    private final TaskChecklistMapper taskChecklistMapper;
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
                .map(taskChecklistMapper::toResponse)
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

        featureEventTrackingService.handle(
                checklistEventFactory.added(
                        trip.getActivity(),
                        currentUser,
                        checklists.size()
                )
        );
    }

    @Override
    public void updateTripChecklistItem(String activityId, Integer checklistId, UpdateChecklistItemRequest request) {
        Trip trip = tripRepository.findByActivityId(activityId).orElseThrow(() -> new ResourceNotFoundException("Trip not found."));
        Checklist checklist = checklistRepository.findById(checklistId).orElseThrow(() -> new ResourceNotFoundException("Checklist item not found."));
        if (checklist.getTrip() == null || !checklist.getTrip().getActivityId().equals(trip.getActivityId())) {
            throw new IllegalArgumentException("Checklist item does not belong to this trip.");
        }
        String oldItemName = checklist.getItemName();

        checklist.setItemName(request.getItemName());
        Checklist saved = checklistRepository.save(checklist);

        User currentUser = currentUserService.getCurrentUser();
        featureEventTrackingService.handle(checklistEventFactory.updated(saved, currentUser, oldItemName));
    }

    @Override
    public List<ChecklistResponse> getTripChecklistItems(String activityId) {
        Trip trip = tripRepository.findByActivityId(activityId).orElseThrow(() -> new ResourceNotFoundException("Trip not found."));
        return checklistRepository
                .findByTripOrderByIdAsc(trip)
                .stream()
                .map(taskChecklistMapper::toResponse)
                .toList();
    }

    @Override
    public void markChecklistCompleted(Integer checklistId) {

        Checklist checklist = checklistRepository
                .findById(checklistId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Checklist item not found."));

        boolean oldCompleted =
                Boolean.TRUE.equals(checklist.getIsCompleted());

        if (oldCompleted) {
            return;
        }

        checklist.setIsCompleted(true);

        Checklist saved = checklistRepository.save(checklist);

        User currentUser = currentUserService.getCurrentUser();

        featureEventTrackingService.handle(
                checklistEventFactory.completed(
                        saved,
                        currentUser
                )
        );
    }


    @Override
    public void addChecklistItem(
            String activityId,
            CreateChecklistItemRequest request) {

        Task task = taskRepository
                .findByActivityId(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found."));

        Checklist checklist = new Checklist();

        checklist.setTask(task);
        checklist.setItemName(request.getItemName());
        checklist.setIsCompleted(false);
        checklist.setCreatedAt(LocalDateTime.now());

        Checklist saved = checklistRepository.save(checklist);

        User currentUser = currentUserService.getCurrentUser();

        featureEventTrackingService.handle(
                checklistEventFactory.added(
                        task.getActivity(),
                        currentUser,
                        1
                )
        );
    }


    @Override
    public void updateChecklistItem(
            Integer checklistId,
            UpdateChecklistItemRequest request) {

        Checklist checklist = checklistRepository
                .findById(checklistId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Checklist item not found."));

        String oldItemName = checklist.getItemName();

        checklist.setItemName(request.getItemName());

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
    public void toggleChecklistCompletion(Integer checklistId) {

        Checklist checklist = checklistRepository
                .findById(checklistId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Checklist item not found."));

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

        Checklist checklist = checklistRepository
                .findById(checklistId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Checklist item not found."));

        checklistRepository.delete(checklist);

        User currentUser = currentUserService.getCurrentUser();

        featureEventTrackingService.handle(
                checklistEventFactory.removed(
                        checklist,
                        currentUser
                )
        );
    }
}