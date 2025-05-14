package com.example.didyouknow.repository;

import com.example.didyouknow.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Comment
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTargetTypeAndTargetId(String targetType, Long targetId);
    List<Comment> findByParentComment(Comment parentComment);
}
