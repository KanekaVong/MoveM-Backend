package com.movem.backend.Service.Implement.TaskServices;

import com.movem.backend.Dto.request.TaskRequests.Create.CreateChecklistItemRequest;
import com.movem.backend.Dto.request.TaskRequests.Update.UpdateChecklistItemRequest;
import com.movem.backend.Dto.response.TaskResponses.TaskChecklistResponse;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Tasks.Task;
import com.movem.backend.Entity.Tasks.TaskChecklist;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.TaskMapper.TaskChecklistMapper;
import com.movem.backend.Repository.TaskRepositories.TaskChecklistRepository;
import com.movem.backend.Repository.TaskRepositories.TaskRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.Event.Factory.ChecklistEventFactory;
import com.movem.backend.Service.Event.FeatureEventTrackingService;
import com.movem.backend.Service.TaskServices.TaskChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskChecklistServiceImpl
        implements TaskChecklistService {

    private final TaskRepository taskRepository;
    private final TaskChecklistRepository taskChecklistRepository;
    private final CurrentUserService currentUserService;
    private final TaskChecklistMapper taskChecklistMapper;
    private final FeatureEventTrackingService featureEventTrackingService;
    private final ChecklistEventFactory checklistEventFactory;

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

        taskChecklistRepository.saveAll(checklists);

        User currentUser = currentUserService.getCurrentUser();

        featureEventTrackingService.handle(
                checklistEventFactory.added(
                        task.getActivity(),
                        currentUser,
                        checklists.size()
                )
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

        TaskChecklist saved =
                taskChecklistRepository.save(checklist);

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
                taskChecklistRepository.save(checklist);

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
            UpdateChecklistItemRequest request
    ) {

        TaskChecklist checklist = taskChecklistRepository.findById(checklistId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Checklist item not found."));

        String oldItemName = checklist.getItemName();

        checklist.setItemName(request.getItemName());

        TaskChecklist saved = taskChecklistRepository.save(checklist);

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
                Boolean.TRUE.equals(checklist.getIsCompleted());

        boolean newCompleted = !oldCompleted;

        checklist.setIsCompleted(newCompleted);

        TaskChecklist saved =
                taskChecklistRepository.save(checklist);

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

        String deletedItemName = checklist.getItemName();

        Task activityTask = checklist.getTask();

        Integer deletedChecklistId = checklist.getId();

        taskChecklistRepository.delete( checklist );

        User currentUser = currentUserService.getCurrentUser();

        featureEventTrackingService.handle(
                checklistEventFactory.removed(
                        checklist,
                        currentUser
                )
        );

        taskChecklistRepository.delete(checklist);


    }
}