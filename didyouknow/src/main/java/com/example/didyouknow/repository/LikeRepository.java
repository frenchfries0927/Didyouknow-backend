package com.example.didyouknow.repository;

import com.example.didyouknow.domain.Like;
import com.example.didyouknow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Like
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndTargetTypeAndTargetId(User user, String targetType, Long targetId);
    List<Like> findByUser(User user);
}
