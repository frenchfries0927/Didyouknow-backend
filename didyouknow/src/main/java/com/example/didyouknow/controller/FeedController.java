package com.example.didyouknow.controller;


import com.example.didyouknow.dto.feed.FeedResponse;
import com.example.didyouknow.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
@CrossOrigin  // 컨트롤러 레벨에서 CORS 허용

public class FeedController {

    private final FeedService feedService;

    @GetMapping
    public ResponseEntity<List<FeedResponse>> getFeed() {
        return ResponseEntity.ok(feedService.getFeed());
    }
}
