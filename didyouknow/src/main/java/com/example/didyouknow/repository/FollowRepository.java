package com.example.didyouknow.repository;

import com.example.didyouknow.domain.Follow;
import com.example.didyouknow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Follow
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(User follower);
    List<Follow> findByFollowing(User following);
}
