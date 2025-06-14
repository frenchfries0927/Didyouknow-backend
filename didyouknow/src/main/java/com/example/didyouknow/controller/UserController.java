package com.example.didyouknow.controller;

import com.example.didyouknow.auth.CustomUserDetails;
import com.example.didyouknow.common.ApiResponse;
import com.example.didyouknow.common.ApiResponseHelper;
import com.example.didyouknow.dto.user.ProfileRequest;
import com.example.didyouknow.dto.user.UserProfileResponse;
import com.example.didyouknow.dto.post.KnowledgePostResponse;
import com.example.didyouknow.dto.user.UserSearchResponse;
import com.example.didyouknow.service.UserService;
import com.example.didyouknow.auth.JwtProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "유저 도메인 API")
public class UserController {

    private final UserService userService;

    @PatchMapping("/me/complete-profile")
    public ResponseEntity<ApiResponse<Void>> completeProfile(@RequestBody ProfileRequest request,
                                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        userService.completeProfile(userId, request);
        return ApiResponseHelper.success(null);
    }

    @GetMapping("/me/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile(HttpServletRequest request,
                                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        UserProfileResponse profile = userService.getUserProfile(userId);
        return ApiResponseHelper.success(profile);
    }

    @GetMapping("/me/posts")
    public ResponseEntity<ApiResponse<List<KnowledgePostResponse>>> getMyPosts(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<KnowledgePostResponse> posts = userService.getUserPosts(userId);
        return ApiResponseHelper.success(posts);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserSearchResponse>>> searchUsers(@RequestParam("keyword") String keyword,
                                                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
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
