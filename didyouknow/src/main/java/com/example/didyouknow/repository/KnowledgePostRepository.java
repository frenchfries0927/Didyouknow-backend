package com.example.didyouknow.repository;

import com.example.didyouknow.domain.KnowledgePost;
import com.example.didyouknow.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// KnowledgePost
public interface KnowledgePostRepository extends JpaRepository<KnowledgePost, Long> {
    List<KnowledgePost> findByAuthorOrderByCreatedAtDesc(User author);

    @Query(value = """
    SELECT kp.*
    FROM knowledge_post kp
    JOIN (
        SELECT target_id
        FROM likes
        WHERE target_type = 'knowledge'
        GROUP BY target_id
        HAVING COUNT(*) >= :minLikes
    ) l ON kp.id = l.target_id
    ORDER BY kp.created_at DESC
    LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<KnowledgePost> findPopularPostsAboveLikeThreshold(
            @org.springframework.data.repository.query.Param("minLikes") int minLikes,
            @org.springframework.data.repository.query.Param("limit") int limit,
            @org.springframework.data.repository.query.Param("offset") int offset
    );
}
