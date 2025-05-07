package com.example.didyouknow.repository;

import com.example.didyouknow.domain.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Badge
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Optional<Badge> findByCode(String code);
}
