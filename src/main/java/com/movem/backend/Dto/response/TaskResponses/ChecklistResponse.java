package com.movem.backend.Dto.response.TaskResponses;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistResponse {

    private Integer id;

    private String itemName;

    private Boolean completed;

}