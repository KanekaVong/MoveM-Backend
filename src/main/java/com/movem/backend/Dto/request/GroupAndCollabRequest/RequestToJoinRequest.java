package com.movem.backend.Dto.request.GroupAndCollabRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestToJoinRequest {

    @NotBlank
    private String joinToken;

}
