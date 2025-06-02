package com.example.didyouknow.controller;

import com.example.didyouknow.common.ApiResponse;
import com.example.didyouknow.common.ApiResponseHelper;
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
@CrossOrigin
public class CommentController {

    private final CommentService commentService;

    // 댓글 & 대댓글 작성 (userId는 쿼리 파라미터로 받음)
    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> create(@RequestParam("userId") Long userId,
                                                  @RequestBody CommentRequest request) {
        CommentResponse response = commentService.create(userId, request);
        return ApiResponseHelper.success(response);
    }

    // 댓글 목록 조회
    @GetMapping("/target")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getByTarget(@RequestParam("targetType") String targetType,
                                                             @RequestParam("targetId") Long targetId) {
        List<CommentResponse> comments = commentService.findByTarget(targetType, targetId);
        return ApiResponseHelper.success(comments);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("commentId") Long commentId) {
        commentService.delete(commentId);
        return ApiResponseHelper.success(null);
    }
}
