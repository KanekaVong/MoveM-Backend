package com.movem.backend.shared.checklist.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChecklistResponse {
    Integer id;
    String itemName;
    Boolean completed;

}