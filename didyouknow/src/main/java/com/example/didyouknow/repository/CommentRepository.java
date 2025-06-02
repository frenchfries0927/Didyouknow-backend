package com.example.didyouknow.repository;

import com.example.didyouknow.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Comment
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTargetTypeAndTargetId(String targetType, Long targetId);
    List<Comment> findByParentComment(Comment parentComment);
    List<Comment> findByTargetTypeAndTargetIdOrderByCreatedAtAsc(String targetType, Long targetId);
    
    // 특정 타겟의 댓글 개수 조회
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.targetType = :targetType AND c.targetId = :targetId")
    Long countByTargetTypeAndTargetId(@Param("targetType") String targetType, @Param("targetId") Long targetId);
}
