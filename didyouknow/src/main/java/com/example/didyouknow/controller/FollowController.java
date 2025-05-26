package com.example.didyouknow.controller;

import com.example.didyouknow.dto.follow.FollowResponse;
import com.example.didyouknow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{targetUserId}")
    public ResponseEntity<Void> follow(@AuthenticationPrincipal Long userId,
                                       @PathVariable("targetUserId") Long targetUserId) {
        followService.follow(userId, targetUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{targetUserId}")
    public ResponseEntity<Void> unfollow(@AuthenticationPrincipal Long userId,
                                         @PathVariable("targetUserId") Long targetUserId) {
        followService.unfollow(userId, targetUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/following")
    public ResponseEntity<List<FollowResponse>> getMyFollowing(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(followService.getFollowing(userId));
    }

    @GetMapping("/me/followers")
    public ResponseEntity<List<FollowResponse>> getMyFollowers(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(followService.getFollowers(userId));
    }

    @GetMapping("/{targetUserId}/following")
    public ResponseEntity<List<FollowResponse>> getUserFollowing(@PathVariable("targetUserId") Long targetUserId) {
        return ResponseEntity.ok(followService.getFollowing(targetUserId));
    }

    @GetMapping("/{targetUserId}/followers")
    public ResponseEntity<List<FollowResponse>> getUserFollowers(@PathVariable("targetUserId") Long targetUserId) {
        return ResponseEntity.ok(followService.getFollowers(targetUserId));
    }

    @GetMapping("/check/{targetUserId}")
    public ResponseEntity<Boolean> checkFollowStatus(@AuthenticationPrincipal Long userId,
                                                     @PathVariable("targetUserId") Long targetUserId) {
        boolean isFollowing = followService.isFollowing(userId, targetUserId);
        return ResponseEntity.ok(isFollowing);
    }
}
