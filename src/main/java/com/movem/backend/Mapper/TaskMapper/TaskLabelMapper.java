package com.movem.backend.Mapper.TaskMapper;

import com.movem.backend.Dto.response.TaskResponses.TaskLabelResponse;
import com.movem.backend.Entity.Tasks.TaskLabel;
import com.movem.backend.Mapper.AbstractBaseMapper;
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
