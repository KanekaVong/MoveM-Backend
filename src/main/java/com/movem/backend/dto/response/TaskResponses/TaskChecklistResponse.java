package com.movem.backend.dto.response.TaskResponses;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskChecklistResponse {

    private Integer id;

    private String itemName;

    private Boolean completed;

}