package com.example.didyouknow.service;

import com.example.didyouknow.domain.Follow;
import com.example.didyouknow.domain.User;
import com.example.didyouknow.dto.follow.FollowResponse;
import com.example.didyouknow.repository.FollowRepository;
import com.example.didyouknow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public void follow(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 요청자 없음"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 대상 없음"));

        boolean alreadyFollowed = followRepository.findByFollower(follower).stream()
                .anyMatch(f -> f.getFollowing().equals(following));

        if (!alreadyFollowed) {
            Follow follow = new Follow();
            follow.setFollower(follower);
            follow.setFollowing(following);
            follow.setCreatedAt(LocalDateTime.now());
            followRepository.save(follow);
        }
    }

    public void unfollow(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId).orElseThrow();
        User following = userRepository.findById(followingId).orElseThrow();

        followRepository.findByFollower(follower).stream()
                .filter(f -> f.getFollowing().equals(following))
                .findFirst()
                .ifPresent(followRepository::delete);
    }

    public List<FollowResponse> getFollowers(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return followRepository.findByFollowing(user).stream()
                .map(f -> new FollowResponse(
                        f.getFollower().getId(),
                        f.getFollower().getNickname(),
                        f.getFollower().getProfileImageUrl()))
                .toList();
    }

    public List<FollowResponse> getFollowing(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return followRepository.findByFollower(user).stream()
                .map(f -> new FollowResponse(
                        f.getFollowing().getId(),
                        f.getFollowing().getNickname(),
                        f.getFollowing().getProfileImageUrl()))
                .toList();
    }

    public boolean isFollowing(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 요청자 없음"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 대상 없음"));

        return followRepository.findByFollower(follower).stream()
                .anyMatch(f -> f.getFollowing().equals(following));
    }
}
