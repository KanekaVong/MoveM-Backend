package com.movem.backend.dto.response.TaskResponses;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskLabelResponse {

    private Integer id;

    private String name;

    private String color;
}
