package com.movem.backend.Mapper.TaskMapper;

import com.movem.backend.Dto.response.TaskResponses.ChecklistResponse;
import com.movem.backend.Entity.Tasks.Checklist;
import com.movem.backend.Mapper.BaseMapper.AbstractBaseMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskChecklistMapper
        extends AbstractBaseMapper<Checklist, ChecklistResponse> {

    @Override
    public ChecklistResponse toResponse(Checklist checklist) {

        if (checklist == null) {
            return null;
        }

        return ChecklistResponse.builder()
                .id(checklist.getId())
                .itemName(checklist.getItemName())
                .completed(checklist.getIsCompleted())
                .build();
    }

}