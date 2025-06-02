package com.example.didyouknow.controller;

import com.example.didyouknow.dto.comment.CommentRequest;
import com.example.didyouknow.dto.comment.CommentResponse;
import com.example.didyouknow.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 & 대댓글 작성 (userId는 쿼리 파라미터로 받음)
    @PostMapping
    public ResponseEntity<CommentResponse> create(@RequestParam Long userId,
                                                  @RequestBody CommentRequest request) {
        CommentResponse response = commentService.create(userId, request);
        return ResponseEntity.ok(response);
    }

    // 댓글 목록 조회
    @GetMapping("/target")
    public ResponseEntity<List<CommentResponse>> getByTarget(@RequestParam String targetType,
                                                             @RequestParam Long targetId) {
        return ResponseEntity.ok(commentService.findByTarget(targetType, targetId));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable("commentId") Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }
}
