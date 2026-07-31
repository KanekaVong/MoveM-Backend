package com.movem.backend.repository.FriendRepository;

import com.movem.backend.entity.User;
import com.movem.backend.entity.Friend.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository
        extends JpaRepository<Friend, Long> {

    Optional<Friend> findByUserOneAndUserTwo(
            User userOne,
            User userTwo
    );

    List<Friend> findByUserOneOrUserTwo(
            User userOne,
            User userTwo
    );

    boolean existsByUserOneAndUserTwo(
            User userOne,
            User userTwo
    );

    List<Friend> findByUserOneIdOrUserTwoId(
            Integer userOneId,
            Integer userTwoId
    );

    Optional<Friend> findByUserOneIdAndUserTwoId(
            Integer userOneId,
            Integer userTwoId
    );
}