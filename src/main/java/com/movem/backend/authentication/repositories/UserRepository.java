package com.movem.backend.authentication.repositories;

import com.movem.backend.authentication.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    List<User> findByUsernameContainingIgnoreCaseOrFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCase(String username, String firstname, String lastname);
    List<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String email);
    @Query("""
    SELECT u
    FROM User u
    WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(u.firstname) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(u.email) LIKE LOWER(CONCAT(:keyword, '%'))
    """)
    Page<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
    SELECT u
    FROM User u
    WHERE u.id <> :currentUserId

    AND NOT EXISTS (
        SELECT 1
        FROM Friend f
        WHERE
            (f.userOne.id = :currentUserId AND f.userTwo.id = u.id)
            OR
            (f.userOne.id = u.id AND f.userTwo.id = :currentUserId)
    )

    AND NOT EXISTS (
        SELECT 1
        FROM FriendRequest fr
        WHERE
            fr.sender.id = :currentUserId
            AND fr.receiver.id = u.id
            AND fr.status = com.movem.backend.commons.enums.Friend.FriendRequestStatus.PENDING
    )

    AND NOT EXISTS (
        SELECT 1
        FROM FriendRequest fr
        WHERE
            fr.sender.id = u.id
            AND fr.receiver.id = :currentUserId
            AND fr.status = com.movem.backend.commons.enums.Friend.FriendRequestStatus.PENDING
    )
""")
    List<User> findSuggestedFriends(@Param("currentUserId") Integer currentUserId);
}