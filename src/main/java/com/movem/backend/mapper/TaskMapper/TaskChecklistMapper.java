package com.movem.backend.mapper.TaskMapper;


import com.movem.backend.dto.response.TaskResponses.TaskChecklistResponse;
import com.movem.backend.entity.Tasks.TaskChecklist;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskChecklistMapper {

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

    public List<TaskChecklistResponse> toResponseList(
            List<TaskChecklist> checklists
    ) {

        return checklists.stream()
                .map(this::toResponse)
                .toList();
    }
}