package com.example.didyouknow.repository;

import com.example.didyouknow.domain.Like;
import com.example.didyouknow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// Like
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndTargetTypeAndTargetId(User user, String targetType, Long targetId);
    List<Like> findByUser(User user);

    

    boolean existsByUserAndTargetTypeAndTargetId(User user, String targetType, Long targetId);

    void deleteByUserIdAndTargetId(User user, Long targetId);

    void deleteByUserAndTargetId(User user, Long targetId);
    
    // 특정 타겟의 좋아요 개수 조회
    @Query("SELECT COUNT(l) FROM Like l WHERE l.targetType = :targetType AND l.targetId = :targetId")
    Long countByTargetTypeAndTargetId(@Param("targetType") String targetType, @Param("targetId") Long targetId);
    
    // 특정 사용자가 특정 타겟에 좋아요를 눌렀는지 확인
    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Like l WHERE l.user.id = :userId AND l.targetType = :targetType AND l.targetId = :targetId")
    Boolean isLikedByUser(@Param("userId") Long userId, @Param("targetType") String targetType, @Param("targetId") Long targetId);
}
