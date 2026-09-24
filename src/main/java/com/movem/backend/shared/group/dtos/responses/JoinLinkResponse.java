package com.movem.backend.shared.group.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinLinkResponse {

    private String joinToken;

    private String joinLink;
}
