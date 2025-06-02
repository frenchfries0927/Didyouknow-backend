package com.example.didyouknow.controller;

import com.example.didyouknow.common.ApiResponse;
import com.example.didyouknow.common.ApiResponseHelper;
import com.example.didyouknow.dto.post.KnowledgePostRequest;
import com.example.didyouknow.dto.post.KnowledgePostResponse;
import com.example.didyouknow.service.FileStorageService;
import com.example.didyouknow.service.KnowledgePostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "KnowledgePost API", description = "지식게시글 도메인 API")
public class PostController {

    private final KnowledgePostService knowledgePostService;
    private final FileStorageService fileStorageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<KnowledgePostResponse>> create(
            @RequestParam("userId") Long userId,
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

