package com.example.didyouknow.controller;


import com.example.didyouknow.common.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<FeedResponse>>> getFeed() {
        List<FeedResponse> feedList = feedService.getFeed();
        ApiResponse<List<FeedResponse>> response = ApiResponse.of(200, "success", feedList);
        return ResponseEntity.ok(response);
    }
}
