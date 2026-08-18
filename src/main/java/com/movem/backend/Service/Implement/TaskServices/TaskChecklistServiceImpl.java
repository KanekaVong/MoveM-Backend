package com.movem.backend.Service.Implement.TaskServices;

import com.movem.backend.Dto.request.TaskRequests.Create.CreateChecklistItemRequest;
import com.movem.backend.Dto.request.TaskRequests.Update.UpdateChecklistItemRequest;
import com.movem.backend.Dto.response.TaskResponses.TaskChecklistResponse;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Tasks.Task;
import com.movem.backend.Entity.Tasks.TaskChecklist;
import com.movem.backend.Event.FeatureEvent;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.TaskMapper.TaskChecklistMapper;
import com.movem.backend.Repository.TaskRepositories.TaskChecklistRepository;
import com.movem.backend.Repository.TaskRepositories.TaskRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.SharedServices.Event.FeatureEventTrackingService;
import com.movem.backend.Service.TaskServices.TaskChecklistService;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.model.enums.Event.FeatureEventAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskChecklistServiceImpl
        implements TaskChecklistService {

    private final TaskRepository taskRepository;
    private final TaskChecklistRepository taskChecklistRepository;
    private final CurrentUserService currentUserService;
    private final TaskChecklistMapper taskChecklistMapper;
    private final FeatureEventTrackingService featureEventTrackingService;


    @Override
    public void createChecklistItems(
            Task task,
            List<CreateChecklistItemRequest> items
    ) {

        if (items == null || items.isEmpty()) {
            return;
        }

        List<TaskChecklist> checklists =
                new ArrayList<>();

        for (CreateChecklistItemRequest item : items) {

            TaskChecklist checklist =
                    new TaskChecklist();

            checklist.setTask(task);
            checklist.setItemName(
                    item.getItemName()
            );
            checklist.setIsCompleted(false);
            checklist.setCreatedAt(
                    LocalDateTime.now()
            );

            checklists.add(checklist);
        }

        taskChecklistRepository.saveAll(
                checklists
        );

        User currentUser =
                currentUserService.getCurrentUser();

        featureEventTrackingService.handle(
                FeatureEvent.builder()
                        .activity(task.getActivity())
                        .actor(currentUser)
                        .feedEvent(ActivityFeedEvent.CHECKLIST_ADDED)
                        .feedMessage("added checklist items.")
                        .auditCategory(AuditCategory.TASK)
                        .auditSeverity(AuditSeverity.INFO)
                        .auditEntity("checklist")
                        .auditMessage("Added checklist items.")
                        .oldValue(null)
                        .newValue(String.valueOf(checklists.size()))
                        .referenceId(null)
                        .actions(Set.of(FeatureEventAction.ACTIVITY_FEED, FeatureEventAction.AUDIT_LOG))
                        .build()
        );
    }


    @Override
    public List<TaskChecklistResponse> getChecklistItems(
            String activityId
    ) {

        Task task =
                taskRepository.findByActivityId(
                        activityId
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found."
                        )
                );

        return taskChecklistRepository
                .findByTaskOrderByIdAsc(task)
                .stream()
                .map(taskChecklistMapper::toResponse)
                .toList();
    }


    @Override
    public void markChecklistCompleted(
            Integer checklistId
    ) {

        TaskChecklist checklist =
                taskChecklistRepository
                        .findById(checklistId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Checklist item not found."
                                )
                        );

        boolean oldCompleted =
                Boolean.TRUE.equals(
                        checklist.getIsCompleted()
                );

        if (oldCompleted) {
            return;
        }

        checklist.setIsCompleted(true);

        taskChecklistRepository.save(
                checklist
        );

        User currentUser =
                currentUserService.getCurrentUser();

        featureEventTrackingService.handle(
                FeatureEvent.builder()
                        .activity(checklist.getTask().getActivity())
                        .actor(currentUser)
                        .feedEvent(ActivityFeedEvent.CHECKLIST_COMPLETED)
                        .feedMessage("completed a checklist item.")
                        .auditCategory(AuditCategory.TASK)
                        .auditSeverity(AuditSeverity.INFO)
                        .auditEntity("checklist")
                        .auditMessage("Completed checklist item.")
                        .oldValue("INCOMPLETE")
                        .newValue("COMPLETED")
                        .referenceId(String.valueOf(checklist.getId()))
                        .actions(Set.of(FeatureEventAction.ACTIVITY_FEED, FeatureEventAction.AUDIT_LOG))
                        .build()
        );
    }


    @Override
    public void addChecklistItem(
            String activityId,
            CreateChecklistItemRequest request
    ) {

        Task task =
                taskRepository.findByActivityId(
                        activityId
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found."
                        )
                );

        TaskChecklist checklist =
                new TaskChecklist();

        checklist.setTask(task);
        checklist.setItemName(
                request.getItemName()
        );
        checklist.setIsCompleted(false);

        TaskChecklist saved =
                taskChecklistRepository.save(
                        checklist
                );

        User currentUser =
                currentUserService.getCurrentUser();

        featureEventTrackingService.handle(
                FeatureEvent.builder()
                        .activity(task.getActivity())
                        .actor(currentUser)
                        .feedEvent(ActivityFeedEvent.CHECKLIST_ADDED)
                        .feedMessage("added a checklist item.")
                        .auditCategory(AuditCategory.TASK)
                        .auditSeverity(AuditSeverity.INFO)
                        .auditEntity("checklist")
                        .auditMessage("Added checklist item.")
                        .oldValue(null)
                        .newValue(saved.getItemName())
                        .referenceId(String.valueOf(saved.getId()))
                        .actions(Set.of(FeatureEventAction.ACTIVITY_FEED, FeatureEventAction.AUDIT_LOG))
                        .build()
        );
    }


    @Override
    public void updateChecklistItem(
            Integer checklistId,
            UpdateChecklistItemRequest request
    ) {

        TaskChecklist checklist =
                taskChecklistRepository
                        .findById(checklistId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Checklist item not found."
                                )
                        );

        String oldItemName =
                checklist.getItemName();

        checklist.setItemName(
                request.getItemName()
        );

        TaskChecklist saved =
                taskChecklistRepository.save(
                        checklist
                );

        User currentUser =
                currentUserService.getCurrentUser();

        featureEventTrackingService.handle(
                FeatureEvent.builder()
                        .activity(saved.getTask().getActivity())
                        .actor(currentUser)
                        .feedEvent(ActivityFeedEvent.CHECKLIST_UPDATED)
                        .feedMessage("updated a checklist item.")
                        .auditCategory(AuditCategory.TASK)
                        .auditSeverity(AuditSeverity.INFO)
                        .auditEntity("checklist")
                        .auditMessage("Updated checklist item.")
                        .oldValue(oldItemName)
                        .newValue(saved.getItemName())
                        .referenceId(String.valueOf(saved.getId()))
                        .actions(Set.of(FeatureEventAction.ACTIVITY_FEED, FeatureEventAction.AUDIT_LOG))
                        .build()
        );
    }

    @Override
    public void toggleChecklistCompletion(
            Integer checklistId
    ) {

        TaskChecklist checklist =
                taskChecklistRepository
                        .findById(checklistId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Checklist item not found."
                                )
                        );

        boolean oldCompleted =
                Boolean.TRUE.equals(
                        checklist.getIsCompleted()
                );

        boolean newCompleted =
                !oldCompleted;

        checklist.setIsCompleted(
                newCompleted
        );

        TaskChecklist saved =
                taskChecklistRepository.save(
                        checklist
                );

        User currentUser =
                currentUserService.getCurrentUser();

        featureEventTrackingService.handle(
                FeatureEvent.builder()
                        .activity(saved.getTask().getActivity())
                        .actor(currentUser)
                        .feedEvent( ActivityFeedEvent.CHECKLIST_COMPLETED)
                        .feedMessage(newCompleted ? "completed a checklist item." : "marked a checklist item as incomplete.")
                        .auditCategory(AuditCategory.TASK)
                        .auditSeverity(AuditSeverity.INFO)
                        .auditEntity("checklist")
                        .auditMessage(newCompleted ? "Completed checklist item." : "Marked checklist item as incomplete.")
                        .oldValue(oldCompleted ? "COMPLETED" : "INCOMPLETE")
                        .newValue(newCompleted ? "COMPLETED" : "INCOMPLETE")
                        .referenceId(String.valueOf(saved.getId()))
                        .actions(Set.of(FeatureEventAction.ACTIVITY_FEED, FeatureEventAction.AUDIT_LOG))
                        .build()
        );
    }


    @Override
    public void deleteChecklistItem(
            Integer checklistId
    ) {

        TaskChecklist checklist =
                taskChecklistRepository
                        .findById(checklistId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Checklist item not found."
                                )
                        );

        String deletedItemName =
                checklist.getItemName();

        Task activityTask =
                checklist.getTask();

        Integer deletedChecklistId =
                checklist.getId();

        taskChecklistRepository.delete(
                checklist
        );

        User currentUser =
                currentUserService.getCurrentUser();

        featureEventTrackingService.handle(
                FeatureEvent.builder()
                        .activity(activityTask.getActivity())
                        .actor(currentUser)
                        .feedEvent(ActivityFeedEvent.CHECKLIST_REMOVED)
                        .feedMessage("deleted a checklist item.")
                        .auditCategory(AuditCategory.TASK)
                        .auditSeverity(AuditSeverity.INFO)
                        .auditEntity("checklist")
                        .auditMessage("Deleted checklist item.")
                        .oldValue(deletedItemName)
                        .newValue(null)
                        .referenceId(String.valueOf(deletedChecklistId))
                        .actions(Set.of(FeatureEventAction.ACTIVITY_FEED, FeatureEventAction.AUDIT_LOG))
                        .build()
        );
    }
}