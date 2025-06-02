package com.example.didyouknow.controller;

import com.example.didyouknow.common.ApiResponse;
import com.example.didyouknow.common.ApiResponseHelper;
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
    public ResponseEntity<ApiResponse<Void>> follow(@AuthenticationPrincipal Long userId,
                                       @PathVariable("targetUserId") Long targetUserId) {
        followService.follow(userId, targetUserId);
        return ApiResponseHelper.success(null);
    }

    @DeleteMapping("/{targetUserId}")
    public ResponseEntity<ApiResponse<Void>> unfollow(@AuthenticationPrincipal Long userId,
                                         @PathVariable("targetUserId") Long targetUserId) {
        followService.unfollow(userId, targetUserId);
        return ApiResponseHelper.success(null);
    }

    @GetMapping("/followers")
    public ResponseEntity<ApiResponse<List<FollowResponse>>> getMyFollowers(@AuthenticationPrincipal Long userId) {
        return ApiResponseHelper.success(followService.getFollowers(userId));
    }

    @GetMapping("/following")
    public ResponseEntity<ApiResponse<List<FollowResponse>>> getMyFollowing(@AuthenticationPrincipal Long userId) {
        return ApiResponseHelper.success(followService.getFollowing(userId));
    }

    @GetMapping("/{targetUserId}/following")
    public ResponseEntity<ApiResponse<List<FollowResponse>>> getUserFollowing(@PathVariable("targetUserId") Long targetUserId) {
        return ApiResponseHelper.success(followService.getFollowing(targetUserId));
    }

    @GetMapping("/{targetUserId}/followers")
    public ResponseEntity<ApiResponse<List<FollowResponse>>> getUserFollowers(@PathVariable("targetUserId") Long targetUserId) {
        return ApiResponseHelper.success(followService.getFollowers(targetUserId));
    }

    @GetMapping("/check/{targetUserId}")
    public ResponseEntity<ApiResponse<Boolean>> checkFollowStatus(@AuthenticationPrincipal Long userId,
                                                     @PathVariable("targetUserId") Long targetUserId) {
        boolean isFollowing = followService.isFollowing(userId, targetUserId);
        return ApiResponseHelper.success(isFollowing);
    }
}
