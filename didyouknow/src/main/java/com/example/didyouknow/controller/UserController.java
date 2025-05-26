package com.example.didyouknow.controller;

import com.example.didyouknow.dto.user.ProfileRequest;
import com.example.didyouknow.dto.user.UserProfileResponse;
import com.example.didyouknow.dto.post.KnowledgePostResponse;
import com.example.didyouknow.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @PatchMapping("/me/complete-profile")
    public ResponseEntity<?> completeProfile(@RequestBody ProfileRequest request,
                                             @AuthenticationPrincipal Long userId) {
        System.out.println(request.toString());
        userService.completeProfile(userId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/profile")
    public ResponseEntity<UserProfileResponse> getMyProfile(@AuthenticationPrincipal Long userId) {
        UserProfileResponse profile = userService.getUserProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/me/posts")
    public ResponseEntity<List<KnowledgePostResponse>> getMyPosts(@AuthenticationPrincipal Long userId) {
        List<KnowledgePostResponse> posts = userService.getUserPosts(userId);
        return ResponseEntity.ok(posts);
    }
}
