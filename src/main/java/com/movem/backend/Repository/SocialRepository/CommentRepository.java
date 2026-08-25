package com.movem.backend.Repository.SocialRepository;

import com.movem.backend.Entity.Activity.Activity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import com.movem.backend.Entity.Social.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    long countByActivity(Activity activity);

    @Query("""
    SELECT c.activity.id, COUNT(c)
    FROM Comment c
    WHERE c.activity.id IN :activityIds
    GROUP BY c.activity.id
""")
    List<Object[]> countCommentsByActivityIds(
            @Param("activityIds") List<String> activityIds
    );
}