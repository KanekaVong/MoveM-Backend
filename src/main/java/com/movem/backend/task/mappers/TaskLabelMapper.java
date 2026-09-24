package com.movem.backend.task.mappers;

import com.movem.backend.task.dtos.responses.TaskLabelResponse;
import com.movem.backend.task.entities.TaskLabel;
import com.movem.backend.commons.BaseMapper.AbstractBaseMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskLabelMapper
        extends AbstractBaseMapper<TaskLabel, TaskLabelResponse> {

    @Override
    public TaskLabelResponse toResponse(TaskLabel label) {

        if (label == null) {
            return null;
        }

        return TaskLabelResponse.builder()
                .id(label.getId())
                .name(label.getName())
                .color(label.getColor())
                .build();
    }

}
