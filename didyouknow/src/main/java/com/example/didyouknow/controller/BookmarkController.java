package com.example.didyouknow.controller;

import com.example.didyouknow.auth.CustomUserDetails;
import com.example.didyouknow.dto.bookmark.BookmarkResponse;
import com.example.didyouknow.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<Void> bookmark(@AuthenticationPrincipal CustomUserDetails user,
                                        @RequestParam(value = "targetType") String targetType,
                                        @RequestParam(value = "targetId") Long targetId) {
        long userId = user.getUserId();
        bookmarkService.addBookmark(userId, targetType, targetId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{targetId}")
    public ResponseEntity<Void> unbookmark(@AuthenticationPrincipal CustomUserDetails user,
                                          @PathVariable Long targetId) {
        long userId = user.getUserId();
        bookmarkService.removeBookmark(userId, targetId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookmarkResponse>> myBookmarks(@AuthenticationPrincipal CustomUserDetails user) {
        long userId = user.getUserId();
        return ResponseEntity.ok(bookmarkService.getMyBookmarks(userId));
    }
}
