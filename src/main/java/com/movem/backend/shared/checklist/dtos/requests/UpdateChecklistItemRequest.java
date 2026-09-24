package com.movem.backend.shared.checklist.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateChecklistItemRequest {
    private Integer id;
    @NotBlank
    private String itemName;
    private Boolean isCompleted;

}
