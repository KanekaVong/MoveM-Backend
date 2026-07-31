package com.movem.backend.repository.CommentRepository;

import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.Comment;
import com.movem.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository
        extends JpaRepository<Comment, Long> {

    List<Comment> findByActivityOrderByCreatedAtAsc(
            Activity activity
    );

    List<Comment> findByUser(
            User user
    );

}