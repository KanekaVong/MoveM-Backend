package com.movem.backend.social.friend.dtos.response;

import com.movem.backend.commons.enums.Friend.FriendRequestStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendRequestResponse {
     Long requestId;
     Integer senderId;
     String senderUsername;
     String senderProfilePic;
     Integer receiverId;
     String receiverUsername;
     FriendRequestStatus status;
     LocalDateTime createdAt;
}
