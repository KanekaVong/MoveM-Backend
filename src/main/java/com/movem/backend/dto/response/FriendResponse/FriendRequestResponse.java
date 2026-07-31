package com.movem.backend.dto.response.FriendResponse;

import com.movem.backend.model.enums.Friend.FriendRequestStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FriendRequestResponse {

    private Long requestId;

    private Integer senderId;

    private String senderUsername;

    private String senderProfilePic;

    private Integer receiverId;

    private String receiverUsername;

    private FriendRequestStatus status;

    private LocalDateTime createdAt;

}
