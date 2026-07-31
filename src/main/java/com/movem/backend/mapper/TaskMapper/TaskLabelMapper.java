package com.movem.backend.mapper.TaskMapper;

import com.movem.backend.dto.response.TaskResponses.TaskLabelResponse;
import com.movem.backend.entity.Tasks.TaskLabel;
import com.movem.backend.mapper.AbstractBaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

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
