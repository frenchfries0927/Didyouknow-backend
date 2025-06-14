package com.example.didyouknow.controller;

import com.example.didyouknow.auth.CustomUserDetails;
import com.example.didyouknow.common.ApiResponse;
import com.example.didyouknow.common.ApiResponseHelper;
import com.example.didyouknow.dto.comment.CommentRequest;
import com.example.didyouknow.dto.comment.CommentResponse;
import com.example.didyouknow.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<ApiResponse<CommentResponse>> create(@AuthenticationPrincipal CustomUserDetails user,
                                                  @RequestBody CommentRequest request) {
        long userId = user.getUserId();
        CommentResponse response = commentService.create(userId, request);
        return ApiResponseHelper.success(response);
    }

    // 댓글 목록 조회
    @GetMapping("/target")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getByTarget(@RequestParam("targetType") String targetType,
                                                             @RequestParam("targetId") Long targetId) {
        System.out.println("댓글 조회 요청 - targetType: " + targetType + ", targetId: " + targetId);
        List<CommentResponse> comments = commentService.findByTarget(targetType, targetId);
        System.out.println("댓글 조회 결과 - 댓글 수: " + comments.size());
        return ApiResponseHelper.success(comments);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("commentId") Long commentId) {
        commentService.delete(commentId);
        return ApiResponseHelper.success(null);
    }

    // 댓글 개수 조회
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getCommentCount(@RequestParam("targetType") String targetType,
                                                           @RequestParam("targetId") Long targetId) {
        Long count = commentService.getCommentCount(targetType, targetId);
        return ApiResponseHelper.success(count);
    }
}
