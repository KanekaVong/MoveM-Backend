package com.movem.backend.dto.response.GroupResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinLinkResponse {

    private String joinToken;

    private String joinLink;
}
