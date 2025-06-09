package com.example.didyouknow.controller;

import com.example.didyouknow.auth.CustomUserDetails;
import com.example.didyouknow.dto.like.LikeResponse;
import com.example.didyouknow.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@CrossOrigin
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Void> like(@AuthenticationPrincipal CustomUserDetails user,
                                     @RequestParam(value = "targetType") String targetType,
                                     @RequestParam(value = "targetId") Long targetId) {
        long userId = user.getUserId();
        likeService.addLike(userId, targetType, targetId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{targetId}")
    public ResponseEntity<Void> unlike(@AuthenticationPrincipal CustomUserDetails user,
                                       @PathVariable Long targetId) {
        long userId = user.getUserId();
        likeService.removeLike(userId, targetId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleLike(@AuthenticationPrincipal CustomUserDetails user,
                                                         @RequestParam(value = "targetType") String targetType,
                                                         @RequestParam(value = "targetId") Long targetId) {
        long userId = user.getUserId();
        boolean isLiked = likeService.toggleLike(userId, targetType, targetId);
        Long likeCount = likeService.getLikeCount(targetType, targetId);
        
        return ResponseEntity.ok(Map.of(
            "isLiked", isLiked,
            "likeCount", likeCount
        ));
    }
    
    @GetMapping("/count")
    public ResponseEntity<Long> getLikeCount(@RequestParam(value = "targetType") String targetType,
                                           @RequestParam(value = "targetId") Long targetId) {
        return ResponseEntity.ok(likeService.getLikeCount(targetType, targetId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<LikeResponse>> myLikes(@AuthenticationPrincipal CustomUserDetails user) {
        long userId = user.getUserId();
        return ResponseEntity.ok(likeService.getMyLikes(userId));
    }
}
