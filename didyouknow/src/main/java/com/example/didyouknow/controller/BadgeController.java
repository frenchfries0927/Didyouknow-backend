package com.example.didyouknow.controller;

import com.example.didyouknow.auth.CustomUserDetails;
import com.example.didyouknow.dto.badge.BadgeResponse;
import com.example.didyouknow.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/badges")
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    // 수동 지급 (관리자용)
    @PostMapping("/assign")
    public ResponseEntity<Void> assignBadge(@AuthenticationPrincipal CustomUserDetails user,
                                            @RequestParam Long badgeId) {
        long userId = user.getUserId();
        badgeService.assignBadge(userId, badgeId);
        return ResponseEntity.ok().build();
    }

    // 내 뱃지 목록
    @GetMapping("/my")
    public ResponseEntity<List<BadgeResponse>> myBadges(@RequestParam Long userId) {
        return ResponseEntity.ok(badgeService.getBadges(userId));
    }
}
