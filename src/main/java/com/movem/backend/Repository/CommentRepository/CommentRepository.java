package com.movem.backend.Repository.CommentRepository;

import com.movem.backend.Entity.Activity.Activity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import com.movem.backend.Entity.Shared.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

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