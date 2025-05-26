package com.example.didyouknow.service;

import com.example.didyouknow.domain.User;
import com.example.didyouknow.domain.KnowledgePost;
import com.example.didyouknow.domain.QuizPost;
import com.example.didyouknow.dto.user.ProfileRequest;
import com.example.didyouknow.dto.user.SignupRequest;
import com.example.didyouknow.dto.user.UserResponse;
import com.example.didyouknow.dto.user.UserProfileResponse;
import com.example.didyouknow.dto.user.UserSearchResponse;
import com.example.didyouknow.dto.post.KnowledgePostResponse;
import com.example.didyouknow.repository.UserRepository;
import com.example.didyouknow.repository.KnowledgePostRepository;
import com.example.didyouknow.repository.QuizPostRepository;
import com.example.didyouknow.repository.FollowRepository;
import com.example.didyouknow.repository.UserBadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KnowledgePostRepository knowledgePostRepository;
    private final QuizPostRepository quizPostRepository;
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

        // 게시물 수 계산 (KnowledgePost + QuizPost)
        int knowledgePostsCount = knowledgePostRepository.findByAuthor(user).size();
        int quizPostsCount = quizPostRepository.findByAuthor(user).size();
        int postsCount = knowledgePostsCount + quizPostsCount;
        
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

        // KnowledgePost 조회
        List<KnowledgePost> knowledgePosts = knowledgePostRepository.findByAuthor(user);
        
        // QuizPost 조회
        List<QuizPost> quizPosts = quizPostRepository.findByAuthor(user);
        
        // 모든 게시물을 KnowledgePostResponse 형태로 변환
        List<KnowledgePostResponse> allPosts = new ArrayList<>();
        
        // KnowledgePost 변환
        allPosts.addAll(knowledgePosts.stream()
                .map(this::convertToKnowledgePostResponse)
                .collect(Collectors.toList()));
        
        // QuizPost를 KnowledgePostResponse 형태로 변환
        allPosts.addAll(quizPosts.stream()
                .map(this::convertQuizToKnowledgePostResponse)
                .collect(Collectors.toList()));
        
        // 최신순으로 정렬 (publishDate 기준)
        return allPosts.stream()
                .sorted((a, b) -> b.getPublishDate().compareTo(a.getPublishDate()))
                .collect(Collectors.toList());
    }

    public List<UserSearchResponse> searchUsers(String keyword, Long currentUserId) {
        List<User> users = userRepository.findByNicknameContainingIgnoreCase(keyword);
        User currentUser = userRepository.findById(currentUserId).orElse(null);
        
        return users.stream()
                .filter(user -> !user.getId().equals(currentUserId)) // 자기 자신 제외
                .map(user -> {
                    boolean isFollowing = false;
                    if (currentUser != null) {
                        isFollowing = followRepository.findByFollower(currentUser).stream()
                                .anyMatch(f -> f.getFollowing().equals(user));
                    }
                    return new UserSearchResponse(
                            user.getId(),
                            user.getNickname(),
                            user.getProfileImageUrl(),
                            isFollowing
                    );
                })
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
    
    private KnowledgePostResponse convertQuizToKnowledgePostResponse(QuizPost quiz) {
        List<String> imageUrls = new ArrayList<>();
        if (quiz.getImageUrl() != null && !quiz.getImageUrl().isEmpty()) {
            imageUrls.add(quiz.getImageUrl());
        }

        return new KnowledgePostResponse(
                quiz.getId(),
                quiz.getQuestion(), // 퀴즈 질문을 title로 사용
                "퀴즈: " + quiz.getQuestion(), // content에는 퀴즈임을 표시
                quiz.getAuthor().getNickname(),
                quiz.getPublishDate().toString(),
                imageUrls
        );
    }
}
