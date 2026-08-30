package com.movem.backend.Mapper.TaskMapper;

import com.movem.backend.Dto.response.TaskResponses.TaskChecklistResponse;
import com.movem.backend.Entity.Tasks.TaskChecklist;
import com.movem.backend.Mapper.BaseMapper.AbstractBaseMapper;
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