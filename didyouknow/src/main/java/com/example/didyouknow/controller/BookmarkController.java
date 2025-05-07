package com.example.didyouknow.controller;

import com.example.didyouknow.dto.bookmark.BookmarkResponse;
import com.example.didyouknow.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<Void> bookmark(@RequestParam Long userId,
                                         @RequestParam String targetType,
                                         @RequestParam Long targetId) {
        bookmarkService.addBookmark(userId, targetType, targetId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{targetId}")
    public ResponseEntity<Void> unbookmark(@RequestParam Long userId,
                                           @PathVariable Long targetId) {
        bookmarkService.removeBookmark(userId, targetId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookmarkResponse>> myBookmarks(@RequestParam Long userId) {
        return ResponseEntity.ok(bookmarkService.getMyBookmarks(userId));
    }
}
