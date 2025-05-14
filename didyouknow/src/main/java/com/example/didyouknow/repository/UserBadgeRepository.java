package com.example.didyouknow.repository;

import com.example.didyouknow.domain.Badge;
import com.example.didyouknow.domain.User;
import com.example.didyouknow.domain.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// UserBadge
public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    List<UserBadge> findByUser(User user);

    boolean existsByUserAndBadge(User user, Badge badge);
}
