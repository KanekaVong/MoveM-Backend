package com.movem.backend.repository.CommentRepository;

import com.movem.backend.entity.Activity.Activity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import com.movem.backend.entity.Comment;
import com.movem.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface CommentRepository
        extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {
            "user",
            "activity"
    })

    Page<Comment> findByActivityOrderByCreatedAtAsc(
            Activity activity,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {
            "user",
            "activity"
    })
    Optional<Comment> findWithUserAndActivityById(Long id);

    @Transactional
    @Modifying
    void deleteByActivity(Activity activity);

}