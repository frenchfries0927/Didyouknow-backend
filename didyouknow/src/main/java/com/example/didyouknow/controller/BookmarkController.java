package com.example.didyouknow.controller;

import com.example.didyouknow.dto.bookmark.BookmarkResponse;
import com.example.didyouknow.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<Void> bookmark(@RequestParam(value = "userId") Long userId,
                                        @RequestParam(value = "targetType") String targetType,
                                        @RequestParam(value = "targetId") Long targetId) {
        bookmarkService.addBookmark(userId, targetType, targetId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{targetId}")
    public ResponseEntity<Void> unbookmark(@RequestParam(value = "userId") Long userId,
                                          @PathVariable Long targetId) {
        bookmarkService.removeBookmark(userId, targetId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookmarkResponse>> myBookmarks(@RequestParam(value = "userId") Long userId) {
        return ResponseEntity.ok(bookmarkService.getMyBookmarks(userId));
    }

    @GetMapping("/feed")
    public ResponseEntity<List<Map<String, Object>>> getBookmarkFeed(@RequestParam(value = "userId") Long userId) {
        return ResponseEntity.ok(bookmarkService.getBookmarkFeed(userId));
    }

    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleBookmark(@RequestParam(value = "userId") Long userId,
                                                             @RequestParam(value = "targetType") String targetType,
                                                             @RequestParam(value = "targetId") Long targetId) {
        boolean isBookmarked = bookmarkService.toggleBookmark(userId, targetType, targetId);
        return ResponseEntity.ok(Map.of("isBookmarked", isBookmarked));
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkBookmark(@RequestParam(value = "userId") Long userId,
                                                           @RequestParam(value = "targetType") String targetType,
                                                           @RequestParam(value = "targetId") Long targetId) {
        boolean isBookmarked = bookmarkService.isBookmarked(userId, targetType, targetId);
        return ResponseEntity.ok(Map.of("isBookmarked", isBookmarked));
    }
}
