package com.example.didyouknow.service;

import com.example.didyouknow.domain.Like;
import com.example.didyouknow.domain.User;
import com.example.didyouknow.dto.like.LikeResponse;
import com.example.didyouknow.repository.LikeRepository;
import com.example.didyouknow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    public void addLike(Long userId, String targetType, Long targetId) {
        User user = userRepository.findById(userId).orElseThrow();

        boolean exists = likeRepository.existsByUserAndTargetTypeAndTargetId(user, targetType, targetId);
        if (!exists) {
            Like like = new Like();
            like.setUser(user);
            like.setTargetType(targetType);
            like.setTargetId(targetId);
            like.setCreatedAt(LocalDateTime.now());
            likeRepository.save(like);
        }
    }

    public void removeLike(Long userId, Long targetId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        likeRepository.deleteByUserAndTargetId(user, targetId);
    }

    @Transactional
    public synchronized boolean toggleLike(Long userId, String targetType, Long targetId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        
        // 트랜잭션 내에서 다시 한번 체크
        Optional<Like> existingLike = likeRepository.findByUserAndTargetTypeAndTargetId(user, targetType, targetId);
        
        if (existingLike.isPresent()) {
            // 좋아요가 이미 있으면 제거
            likeRepository.delete(existingLike.get());
            likeRepository.flush(); // 즉시 DB에 반영
            return false;
        } else {
            // 좋아요가 없으면 추가
            Like like = new Like();
            like.setUser(user);
            like.setTargetType(targetType);
            like.setTargetId(targetId);
            like.setCreatedAt(LocalDateTime.now());
            likeRepository.save(like);
            likeRepository.flush(); // 즉시 DB에 반영
            return true;
        }
    }
    
    public Long getLikeCount(String targetType, Long targetId) {
        return likeRepository.countByTargetTypeAndTargetId(targetType, targetId);
    }

    public List<LikeResponse> getMyLikes(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return likeRepository.findByUser(user).stream()
                .map(like -> new LikeResponse(
                        like.getTargetType(),
                        like.getTargetId(),
                        like.getCreatedAt().toString()
                )).toList();
    }

}
