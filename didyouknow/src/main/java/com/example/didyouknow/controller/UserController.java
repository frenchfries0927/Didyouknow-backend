package com.example.didyouknow.controller;

import com.example.didyouknow.common.ApiResponse;
import com.example.didyouknow.common.ApiResponseHelper;
import com.example.didyouknow.dto.user.ProfileRequest;
import com.example.didyouknow.dto.user.UserProfileResponse;
import com.example.didyouknow.dto.post.KnowledgePostResponse;
import com.example.didyouknow.dto.user.UserSearchResponse;
import com.example.didyouknow.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "유저 도메인 API")
public class UserController {

    private final UserService userService;

    @PatchMapping("/me/complete-profile")
    public ResponseEntity<ApiResponse<Void>> completeProfile(@RequestBody ProfileRequest request,
                                                             @AuthenticationPrincipal Long userId) {
        userService.completeProfile(userId, request);
        return ApiResponseHelper.success(null);
    }

    @GetMapping("/me/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile(@AuthenticationPrincipal Long userId) {
        UserProfileResponse profile = userService.getUserProfile(userId);
        return ApiResponseHelper.success(profile);
    }

    @GetMapping("/me/posts")
    public ResponseEntity<ApiResponse<List<KnowledgePostResponse>>> getMyPosts(@AuthenticationPrincipal Long userId) {
        List<KnowledgePostResponse> posts = userService.getUserPosts(userId);
        return ApiResponseHelper.success(posts);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserSearchResponse>>> searchUsers(@RequestParam("keyword") String keyword,
                                                                             @AuthenticationPrincipal Long userId) {
        List<UserSearchResponse> users = userService.searchUsers(keyword, userId);
        return ApiResponseHelper.success(users);
    }

    @GetMapping("/{targetUserId}/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(@PathVariable("targetUserId") Long targetUserId) {
        UserProfileResponse profile = userService.getUserProfile(targetUserId);
        return ApiResponseHelper.success(profile);
    }

    @GetMapping("/{targetUserId}/posts")
    public ResponseEntity<ApiResponse<List<KnowledgePostResponse>>> getUserPosts(@PathVariable("targetUserId") Long targetUserId) {
        List<KnowledgePostResponse> posts = userService.getUserPosts(targetUserId);
        return ApiResponseHelper.success(posts);
    }
}
