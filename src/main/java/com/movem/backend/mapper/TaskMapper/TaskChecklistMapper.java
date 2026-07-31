package com.movem.backend.mapper.TaskMapper;

import com.movem.backend.dto.response.TaskResponses.TaskChecklistResponse;
import com.movem.backend.entity.Tasks.TaskChecklist;
import com.movem.backend.mapper.AbstractBaseMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskChecklistMapper
        extends AbstractBaseMapper<TaskChecklist, TaskChecklistResponse> {

    @Override
    public TaskChecklistResponse toResponse(TaskChecklist checklist) {

        if (checklist == null) {
            return null;
        }

        return TaskChecklistResponse.builder()
                .id(checklist.getId())
                .itemName(checklist.getItemName())
                .completed(checklist.getIsCompleted())
                .build();
    }

}