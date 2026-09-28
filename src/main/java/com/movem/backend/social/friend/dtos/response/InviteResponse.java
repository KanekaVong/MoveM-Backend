package com.movem.backend.social.friend.dtos.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InviteResponse {
     Long id;
     String inviteUrl;
     LocalDateTime createdAt;
     LocalDateTime expiresAt;
}