package com.example.didyouknow.controller;

import com.example.didyouknow.dto.follow.FollowResponse;
import com.example.didyouknow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{userId}")
    public ResponseEntity<Void> follow(@RequestParam Long fromUserId,
                                       @PathVariable Long userId) {
        followService.follow(fromUserId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> unfollow(@RequestParam Long fromUserId,
                                         @PathVariable Long userId) {
        followService.unfollow(fromUserId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/following")
    public ResponseEntity<List<FollowResponse>> getFollowing(@RequestParam Long userId) {
        return ResponseEntity.ok(followService.getFollowing(userId));
    }

    @GetMapping("/followers")
    public ResponseEntity<List<FollowResponse>> getFollowers(@RequestParam Long userId) {
        return ResponseEntity.ok(followService.getFollowers(userId));
    }
}
