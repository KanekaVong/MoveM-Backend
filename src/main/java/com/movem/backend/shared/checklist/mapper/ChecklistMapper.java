package com.movem.backend.shared.checklist.mapper;

import com.movem.backend.shared.checklist.dtos.responses.ChecklistResponse;
import com.movem.backend.shared.checklist.entities.Checklist;
import com.movem.backend.commons.BaseMapper.AbstractBaseMapper;
import org.springframework.stereotype.Component;

@Component
public class ChecklistMapper
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