package com.movem.backend.Dto.response.TaskResponses;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskLabelResponse {

    private Integer id;

    private String name;

    private String color;
}
