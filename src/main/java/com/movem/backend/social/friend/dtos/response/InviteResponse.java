package com.movem.backend.social.friend.dtos.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InviteResponse {

    private Long id;

    private String inviteUrl;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}