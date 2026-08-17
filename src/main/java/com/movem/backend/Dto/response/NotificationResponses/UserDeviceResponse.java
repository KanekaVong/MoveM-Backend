package com.movem.backend.Dto.response.NotificationResponses;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserDeviceResponse {

    private Long id;
    private Integer userId;
    private String platform;
    private Boolean isActive;
    private LocalDateTime lastSeenAt;
}