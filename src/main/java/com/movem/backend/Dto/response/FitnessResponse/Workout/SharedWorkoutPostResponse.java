package com.movem.backend.Dto.response.FitnessResponse.Workout;

import com.movem.backend.Dto.response.Attachment.AttachmentResponse;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class SharedWorkoutPostResponse {

    private Integer sessionId;

    private Integer userId;
    private String username;
    private String profilePicture;

    private String workoutType;
    private String trackingMode;

    private String shareDescription;

    private BigDecimal distance;
    private Integer steps;
    private Integer durationSeconds;
    private BigDecimal caloriesBurned;

    private LocalDateTime finishedAt;

    private long kudosCount;
    private boolean myKudos;
    private long commentCount;

    private boolean myPost;

    private List<AttachmentResponse> attachments;
}