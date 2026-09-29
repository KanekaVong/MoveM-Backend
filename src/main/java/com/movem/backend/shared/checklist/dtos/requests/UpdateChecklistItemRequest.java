package com.movem.backend.shared.checklist.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateChecklistItemRequest {
    Integer id;
    @NotBlank
    String itemName;
    Boolean isCompleted;
}
