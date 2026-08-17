package com.movem.backend.Service.Implement.TaskServices;

import com.movem.backend.Dto.request.TaskRequests.Create.CreateChecklistItemRequest;
import com.movem.backend.Dto.request.TaskRequests.Update.UpdateChecklistItemRequest;
import com.movem.backend.Dto.response.TaskResponses.TaskChecklistResponse;
import com.movem.backend.Entity.Tasks.Task;
import com.movem.backend.Entity.Tasks.TaskChecklist;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.TaskMapper.TaskChecklistMapper;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.Repository.TaskRepositories.TaskChecklistRepository;
import com.movem.backend.Repository.TaskRepositories.TaskRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.SharedServices.ActivityFeedService;
import com.movem.backend.Service.SharedServices.AuditLogService;
import com.movem.backend.Service.TaskServices.TaskChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskChecklistServiceImpl implements TaskChecklistService {

    private final TaskRepository taskRepository;
    private final TaskChecklistRepository taskChecklistRepository;
    private final CurrentUserService currentUserService;
    private final ActivityFeedService activityFeedService;
    private final AuditLogService auditLogService;
    private final TaskChecklistMapper taskChecklistMapper;

    @Override
    public void createChecklistItems(
            Task task,
            List<CreateChecklistItemRequest> items
    ) {

        if (items == null || items.isEmpty()) {
            return;
        }

        List<TaskChecklist> checklists = new ArrayList<>();

        for (CreateChecklistItemRequest item : items) {

            TaskChecklist checklist = new TaskChecklist();

            checklist.setTask(task);
            checklist.setItemName(item.getItemName());
            checklist.setIsCompleted(false);
            checklist.setCreatedAt(LocalDateTime.now());

            checklists.add(checklist);
        }

        taskChecklistRepository.saveAll(checklists);

        User currentUser = currentUserService.getCurrentUser();


        activityFeedService.createFeed(
                task.getActivity(),
                currentUser,
                ActivityFeedEvent.CHECKLIST_ADDED,
                "added checklist items.",
                null
        );

        auditLogService.createLog(
                task.getActivity(),
                currentUser,
                ActivityFeedEvent.CHECKLIST_ADDED,
                AuditCategory.TASK,
                AuditSeverity.INFO,
                "checklist",
                "Added checklist items.",
                null,
                String.valueOf(checklists.size())
        );
    }

    @Override
    public List<TaskChecklistResponse> getChecklistItems(
            String activityId
    ) {

        Task task = taskRepository.findByActivityId(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found."));

        return taskChecklistRepository
                .findByTaskOrderByIdAsc(task)
                .stream()
                .map(taskChecklistMapper::toResponse)
                .toList();

    }

    @Override
    public void markChecklistCompleted(Integer id) {

        TaskChecklist checklist =
                taskChecklistRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Checklist item not found."));

        checklist.setIsCompleted(true);

        taskChecklistRepository.save(checklist);

        User currentUser = currentUserService.getCurrentUser();

        activityFeedService.createFeed(
                checklist.getTask().getActivity(),
                currentUser,
                ActivityFeedEvent.CHECKLIST_COMPLETED,
                "completed a checklist item.",
                Long.valueOf(checklist.getId())
        );

        auditLogService.createLog(
                checklist.getTask().getActivity(),
                currentUser,
                ActivityFeedEvent.CHECKLIST_COMPLETED,
                AuditCategory.TASK,
                AuditSeverity.INFO,
                "checklist",
                "Completed checklist item.",
                "INCOMPLETE",
                "COMPLETED"
        );
    }

    @Override
    public void addChecklistItem(
            String activityId,
            CreateChecklistItemRequest request
    ) {

        Task task = taskRepository.findByActivityId(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found."));

        TaskChecklist checklist = new TaskChecklist();

        checklist.setTask(task);
        checklist.setItemName(request.getItemName());
        checklist.setIsCompleted(false);

        taskChecklistRepository.save(checklist);
    }

    @Override
    public void updateChecklistItem(
            Integer checklistId,
            UpdateChecklistItemRequest request
    ) {

        TaskChecklist checklist = taskChecklistRepository.findById(checklistId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Checklist item not found."));

        checklist.setItemName(request.getItemName());

        taskChecklistRepository.save(checklist);
    }

    @Override
    public void toggleChecklistCompletion(
            Integer checklistId
    ) {

        TaskChecklist checklist = taskChecklistRepository.findById(checklistId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Checklist item not found."));

        checklist.setIsCompleted(!checklist.getIsCompleted());

        taskChecklistRepository.save(checklist);
    }

    @Override
    public void deleteChecklistItem(
            Integer checklistId
    ) {

        TaskChecklist checklist = taskChecklistRepository.findById(checklistId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Checklist item not found."));

        taskChecklistRepository.delete(checklist);
    }


}