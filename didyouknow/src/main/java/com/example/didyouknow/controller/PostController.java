package com.example.didyouknow.controller;

import com.example.didyouknow.auth.CustomUserDetails;
import com.example.didyouknow.common.ApiResponse;
import com.example.didyouknow.common.ApiResponseHelper;
import com.example.didyouknow.dto.post.KnowledgePostRequest;
import com.example.didyouknow.dto.post.KnowledgePostResponse;
import com.example.didyouknow.service.FileStorageService;
import com.example.didyouknow.service.KnowledgePostService;
import com.example.didyouknow.service.QuizPostService;
import com.example.didyouknow.dto.quiz.QuizPostResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "KnowledgePost API", description = "지식게시글 도메인 API")
public class PostController {

    private final KnowledgePostService knowledgePostService;
    private final QuizPostService quizPostService;
    private final FileStorageService fileStorageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<KnowledgePostResponse>> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "publishDate", required = false, defaultValue = "2025-01-01") String publishDate,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        try {
            List<String> imageUrls = new ArrayList<>();
            if (images != null && !images.isEmpty()) {
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        String imageUrl = fileStorageService.storeFile(image);
                        imageUrls.add(imageUrl);
                    }
                }
            }

            KnowledgePostRequest request = new KnowledgePostRequest();
            request.setTitle(title);
            request.setContent(content);
            request.setPublishDate(publishDate);

            long userId = userDetails.getUserId();
            KnowledgePostResponse response = knowledgePostService.create(userId, request, imageUrls);
            return ApiResponseHelper.success(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponseHelper.error(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<KnowledgePostResponse>>> findAll() {
        return ApiResponseHelper.success(knowledgePostService.findAll());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<KnowledgePostResponse>> findById(@PathVariable("postId") Long postId) {
        KnowledgePostResponse post = knowledgePostService.findById(postId);
        return ApiResponseHelper.success(post);
    }

    // 통합 게시물 조회 API - 타입에 관계없이 ID로 조회
    @GetMapping("/{postId}/unified")
    public ResponseEntity<ApiResponse<Map<String, Object>>> findUnifiedPost(@PathVariable("postId") Long postId) {
        try {
            // 먼저 지식 게시물에서 찾기
            try {
                KnowledgePostResponse knowledgePost = knowledgePostService.findById(postId);
                Map<String, Object> response = new HashMap<>();
                response.put("id", knowledgePost.getId());
                response.put("type", "knowledge");
                response.put("title", knowledgePost.getTitle());
                response.put("content", knowledgePost.getContent());
                response.put("authorNickname", knowledgePost.getAuthorNickname());
                response.put("publishDate", knowledgePost.getPublishDate());
                response.put("imageUrls", knowledgePost.getImageUrls());
                response.put("options", null);
                response.put("likes", knowledgePost.getLikes());
                response.put("comments", knowledgePost.getComments());
                response.put("isLiked", knowledgePost.getIsLiked());
                
                return ApiResponseHelper.success(response);
            } catch (Exception e) {
                // 지식 게시물에서 찾지 못하면 퀴즈에서 찾기
                try {
                    QuizPostResponse quizPost = quizPostService.findById(postId);
                    Map<String, Object> response = new HashMap<>();
                    response.put("id", quizPost.getId());
                    response.put("type", "quiz");
                    response.put("title", quizPost.getQuestion());
                    response.put("content", quizPost.getQuestion());
                    response.put("authorNickname", quizPost.getAuthorNickname());
                    response.put("publishDate", quizPost.getPublishDate());
                    response.put("imageUrls", quizPost.getImageUrls());
                    response.put("options", quizPost.getOptions());
                    response.put("likes", quizPost.getLikes());
                    response.put("comments", quizPost.getComments());
                    response.put("isLiked", quizPost.getIsLiked());
                    
                    return ApiResponseHelper.success(response);
                } catch (Exception ex) {
                    return ApiResponseHelper.error(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponseHelper.error(HttpStatus.INTERNAL_SERVER_ERROR, "게시물 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/{postId}/detail")
    public ResponseEntity<KnowledgePostResponse> getPostDetail(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(knowledgePostService.findById(postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("postId") Long postId) {
        knowledgePostService.delete(postId);
        return ApiResponseHelper.success(null);
    }
}

