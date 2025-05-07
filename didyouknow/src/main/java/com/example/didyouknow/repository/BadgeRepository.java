package com.example.didyouknow.repository;

import com.example.didyouknow.domain.Badge;
import com.example.didyouknow.domain.User;
import com.example.didyouknow.domain.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Badge
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Optional<Badge> findByCode(String code);
    



}
