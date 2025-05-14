package com.example.didyouknow.controller;

import com.example.didyouknow.dto.like.LikeResponse;
import com.example.didyouknow.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Void> like(@RequestParam Long userId,
                                     @RequestParam String targetType,
                                     @RequestParam Long targetId) {
        likeService.addLike(userId, targetType, targetId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{targetId}")
    public ResponseEntity<Void> unlike(@RequestParam Long userId,
                                       @PathVariable Long targetId) {
        likeService.removeLike(userId, targetId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<LikeResponse>> myLikes(@RequestParam Long userId) {
        return ResponseEntity.ok(likeService.getMyLikes(userId));
    }
}
