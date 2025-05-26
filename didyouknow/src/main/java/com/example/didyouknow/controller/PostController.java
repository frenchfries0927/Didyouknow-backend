package com.example.didyouknow.controller;

import com.example.didyouknow.dto.post.KnowledgePostRequest;
import com.example.didyouknow.dto.post.KnowledgePostResponse;
import com.example.didyouknow.service.FileStorageService;
import com.example.didyouknow.service.KnowledgePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final KnowledgePostService knowledgePostService;
    private final FileStorageService fileStorageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KnowledgePostResponse> create(
            @RequestParam("userId") Long userId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "publishDate", required = false, defaultValue = "2025-01-01") String publishDate,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        
        try {
            // 이미지 업로드 처리
            List<String> imageUrls = new ArrayList<>();
            if (images != null && !images.isEmpty()) {
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        String imageUrl = fileStorageService.storeFile(image);
                        imageUrls.add(imageUrl);
                    }
                }
            }

            // KnowledgePostRequest 객체 생성
            KnowledgePostRequest request = new KnowledgePostRequest();
            request.setTitle(title);
            request.setContent(content);
            request.setPublishDate(publishDate);
            
            KnowledgePostResponse response = knowledgePostService.create(userId, request, imageUrls);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("게시글 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<KnowledgePostResponse>> findAll() {
        return ResponseEntity.ok(knowledgePostService.findAll());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<KnowledgePostResponse> findById(@PathVariable Long postId) {
        return ResponseEntity.ok(knowledgePostService.findById(postId));
    }

    @GetMapping("/detail/{postId}")
    public ResponseEntity<KnowledgePostResponse> getPostDetail(@PathVariable Long postId) {
        return ResponseEntity.ok(knowledgePostService.findById(postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable Long postId) {
        knowledgePostService.delete(postId);
        return ResponseEntity.noContent().build();
    }
}
