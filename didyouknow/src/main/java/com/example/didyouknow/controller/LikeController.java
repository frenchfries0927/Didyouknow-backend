package com.example.didyouknow.controller;

import com.example.didyouknow.dto.like.LikeResponse;
import com.example.didyouknow.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> like(@RequestParam(value = "userId") Long userId,
                                     @RequestParam(value = "targetType") String targetType,
                                     @RequestParam(value = "targetId") Long targetId) {
        likeService.addLike(userId, targetType, targetId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{targetId}")
    public ResponseEntity<Void> unlike(@RequestParam(value = "userId") Long userId,
                                       @PathVariable Long targetId) {
        likeService.removeLike(userId, targetId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleLike(@RequestParam(value = "userId") Long userId,
                                                         @RequestParam(value = "targetType") String targetType,
                                                         @RequestParam(value = "targetId") Long targetId) {
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
    public ResponseEntity<List<LikeResponse>> myLikes(@RequestParam(value = "userId") Long userId) {
        return ResponseEntity.ok(likeService.getMyLikes(userId));
    }
}
