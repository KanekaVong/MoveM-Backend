package com.movem.backend.shared.checklist.dtos.responses;

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