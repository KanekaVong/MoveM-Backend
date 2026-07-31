package com.movem.backend.service.implement.TaskServices;

import com.movem.backend.dto.request.TaskRequests.Create.CreateChecklistItemRequest;
import com.movem.backend.entity.Tasks.Task;
import com.movem.backend.entity.Tasks.TaskChecklist;
import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.repository.TaskRepositories.TaskChecklistRepository;
import com.movem.backend.service.AuthServices.CurrentUserService;
import com.movem.backend.service.SharedServices.ActivityFeedService;
import com.movem.backend.service.SharedServices.AuditLogService;
import com.movem.backend.service.TaskServices.TaskChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskChecklistServiceImpl implements TaskChecklistService {

    private final TaskChecklistRepository taskChecklistRepository;
    private final CurrentUserService currentUserService;
    private final ActivityFeedService activityFeedService;
    private final AuditLogService auditLogService;

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

}