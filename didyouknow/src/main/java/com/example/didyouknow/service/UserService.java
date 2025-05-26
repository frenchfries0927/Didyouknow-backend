package com.example.didyouknow.service;

import com.example.didyouknow.domain.User;
import com.example.didyouknow.domain.KnowledgePost;
import com.example.didyouknow.dto.user.ProfileRequest;
import com.example.didyouknow.dto.user.SignupRequest;
import com.example.didyouknow.dto.user.UserResponse;
import com.example.didyouknow.dto.user.UserProfileResponse;
import com.example.didyouknow.dto.post.KnowledgePostResponse;
import com.example.didyouknow.repository.UserRepository;
import com.example.didyouknow.repository.KnowledgePostRepository;
import com.example.didyouknow.repository.FollowRepository;
import com.example.didyouknow.repository.UserBadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KnowledgePostRepository knowledgePostRepository;
    private final FollowRepository followRepository;
    private final UserBadgeRepository userBadgeRepository;

    public UserResponse signup(SignupRequest request) {
        // 이메일 중복 + 같은 providerId가 있을 경우 중복 가입 방지
        boolean userExists = userRepository.findByEmail(request.getEmail())
                .filter(u -> u.getProviderId().equals(request.getProviderId()))
                .isPresent();

        if (userExists) {
            throw new IllegalArgumentException("이미 가입된 사용자입니다.");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname());
        user.setProfileImageUrl(request.getProfileImageUrl());
        user.setProvider(request.getProvider());
        user.setProviderId(request.getProviderId());
        user.setRole("USER");
        user.setStatus("ACTIVE");
        user.setPushTime(0);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getNickname(),
                saved.getProfileImageUrl(),
                saved.getRole()
        );
    }

    public void completeProfile(Long userId, ProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setNickname(request.getNickname());
        user.setPushTime(request.getPushTime());
        user.setAlarmEnabled(request.isAlarmEnabled());
        user.setStatus("ACTIVE");
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 게시물 수 계산
        int postsCount = knowledgePostRepository.findByAuthor(user).size();
        
        // 팔로워 수 계산
        int followersCount = followRepository.countByFollowing(user);
        
        // 팔로잉 수 계산
        int followingCount = followRepository.countByFollower(user);
        
        // 배지 여부 확인
        boolean hasBadge = userBadgeRepository.existsByUser(user);

        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImageUrl(),
                postsCount,
                followersCount,
                followingCount,
                hasBadge
        );
    }

    public List<KnowledgePostResponse> getUserPosts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<KnowledgePost> posts = knowledgePostRepository.findByAuthor(user);
        
        return posts.stream()
                .map(this::convertToKnowledgePostResponse)
                .collect(Collectors.toList());
    }

    private KnowledgePostResponse convertToKnowledgePostResponse(KnowledgePost post) {
        List<String> imageUrls = post.getImages().stream()
                .map(image -> image.getImageUrl())
                .collect(Collectors.toList());

        return new KnowledgePostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getNickname(),
                post.getPublishDate().toString(),
                imageUrls
        );
    }
}
