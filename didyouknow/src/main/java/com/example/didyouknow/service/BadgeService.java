package com.example.didyouknow.service;

import com.example.didyouknow.domain.Badge;
import com.example.didyouknow.domain.User;
import com.example.didyouknow.domain.UserBadge;
import com.example.didyouknow.dto.badge.BadgeResponse;
import com.example.didyouknow.repository.BadgeRepository;
import com.example.didyouknow.repository.UserBadgeRepository;
import com.example.didyouknow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final UserBadgeRepository userBadgeRepository;
    private final BadgeRepository badgeRepository;
    private final UserRepository userRepository;

    /**
     * 유저에게 뱃지를 수동 지급
     */
    public void assignBadge(Long userId, Long badgeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new IllegalArgumentException("뱃지를 찾을 수 없습니다."));

        boolean alreadyAssigned = userBadgeRepository.existsByUserAndBadge(user, badge);
        if (!alreadyAssigned) {
            UserBadge userBadge = new UserBadge();
            userBadge.setUser(user);
            userBadge.setBadge(badge);
            userBadge.setCreatedAt(LocalDateTime.now());

            userBadgeRepository.save(userBadge);
        }
    }

    /**
     * 유저의 뱃지 목록 조회
     */
    public List<BadgeResponse> getBadges(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        return userBadgeRepository.findByUser(user).stream()
                .map(ub -> new BadgeResponse(
                        ub.getBadge().getId(),
                        ub.getBadge().getCode(),
                        ub.getBadge().getName(),
                        ub.getCreatedAt().toString()
                ))
                .toList();
    }
}
