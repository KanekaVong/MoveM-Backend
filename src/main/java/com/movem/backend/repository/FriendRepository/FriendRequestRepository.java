package com.movem.backend.repository.FriendRepository;

import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Friend.FriendRequestStatus;
import com.movem.backend.entity.Friend.FriendRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository
        extends JpaRepository<FriendRequest, Long> {

    @EntityGraph(attributePaths = {
            "sender",
            "receiver"
    })
    Optional<FriendRequest> findBySenderAndReceiver(
            User sender,
            User receiver
    );

    @EntityGraph(attributePaths = {
            "sender",
            "receiver"
    })
    List<FriendRequest> findByReceiverAndStatus(
            User receiver,
            FriendRequestStatus status
    );

    @EntityGraph(attributePaths = {
            "sender",
            "receiver"
    })
    List<FriendRequest> findBySenderAndStatus(
            User sender,
            FriendRequestStatus status
    );

    @EntityGraph(attributePaths = {
            "sender",
            "receiver"
    })
    Optional<FriendRequest> findBySenderAndReceiverAndStatus(
            User sender,
            User receiver,
            FriendRequestStatus status
    );

    @EntityGraph(attributePaths = {
            "sender",
            "receiver"
    })
    List<FriendRequest> findByReceiverIdAndStatusOrderByCreatedAtDesc(
            Integer receiverId,
            FriendRequestStatus status
    );

    @EntityGraph(attributePaths = {
            "sender",
            "receiver"
    })
    List<FriendRequest> findBySenderIdAndStatusOrderByCreatedAtDesc(
            Integer senderId,
            FriendRequestStatus status
    );

    @Modifying
    @Query("""
    DELETE FROM FriendRequest fr
    WHERE
    (fr.sender = :user1 AND fr.receiver = :user2)
    OR
    (fr.sender = :user2 AND fr.receiver = :user1)
    """)
    void deleteRequestsBetweenUsers(
            @Param("user1") User user1,
            @Param("user2") User user2
    );

}
