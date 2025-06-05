package com.example.didyouknow.repository;

import com.example.didyouknow.domain.QuizPost;
import com.example.didyouknow.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// QuizPost
public interface QuizPostRepository extends JpaRepository<QuizPost, Long> {
    List<QuizPost> findByAuthorOrderByCreatedAtDesc(User author);

    @Query(value = """
    SELECT qp.*
    FROM quiz_post qp
    JOIN (
        SELECT target_id
        FROM likes
        WHERE target_type = 'quiz'
        GROUP BY target_id
        HAVING COUNT(*) >= :minLikes
    ) l ON qp.id = l.target_id
    ORDER BY qp.created_at DESC
    LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<QuizPost> findPopularPostsAboveLikeThreshold(int minLikes, int limit, int offset);
}
