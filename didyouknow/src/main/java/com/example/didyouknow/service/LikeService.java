package com.example.didyouknow.service;

import com.example.didyouknow.domain.Like;
import com.example.didyouknow.domain.User;
import com.example.didyouknow.dto.like.LikeResponse;
import com.example.didyouknow.repository.LikeRepository;
import com.example.didyouknow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
