package com.movem.backend.shared.group.dtos.responses;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JoinLinkResponse {
     String joinToken;
     String joinLink;
}
