package com.example.didyouknow.controller;

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
    private final JwtProvider jwtProvider;

    // JWT에서 사용자 ID 추출하는 헬퍼 메서드
    private Long extractUserIdFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");
            if (jwtProvider.validateToken(token)) {
                return jwtProvider.getUserIdFromToken(token);
            }
        }
        return null;
    }

    @PatchMapping("/me/complete-profile")
    public ResponseEntity<ApiResponse<Void>> completeProfile(@RequestBody ProfileRequest request,
                                             HttpServletRequest httpRequest) {
        Long userId = extractUserIdFromRequest(httpRequest);
        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        userService.completeProfile(userId, request);
        return ApiResponseHelper.success(null);
    }

    @GetMapping("/me/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile(HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        UserProfileResponse profile = userService.getUserProfile(userId);
        return ApiResponseHelper.success(profile);
    }

    @GetMapping("/me/posts")
    public ResponseEntity<ApiResponse<List<KnowledgePostResponse>>> getMyPosts(HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        List<KnowledgePostResponse> posts = userService.getUserPosts(userId);
        return ApiResponseHelper.success(posts);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserSearchResponse>>> searchUsers(@RequestParam("keyword") String keyword,
                                                                             HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
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
