package com.movem.backend.Dto.response.FriendResponse;

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