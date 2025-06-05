package com.example.didyouknow.controller;


import com.example.didyouknow.common.ApiResponse;
import com.example.didyouknow.dto.feed.FeedResponse;
import com.example.didyouknow.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<ApiResponse<List<FeedResponse>>> getFeed(@RequestParam(value = "userId", required = false) Long userId) {
        List<FeedResponse> feedList = feedService.getFeedForUser(userId);
        ApiResponse<List<FeedResponse>> response = ApiResponse.of(200, "success", feedList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<Slice<FeedResponse>>> getPopularFeed(
            @PageableDefault(size = 10, sort = "likes", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "userId", required = false) Long userId
    ) {
        Slice<FeedResponse> feeds = feedService.getPopularFeeds(pageable, userId);
        return ResponseEntity.ok(ApiResponse.of(200, "success", feeds));
    }
}
