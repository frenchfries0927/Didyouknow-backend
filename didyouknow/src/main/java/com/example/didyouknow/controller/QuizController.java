package com.example.didyouknow.controller;

import com.example.didyouknow.dto.quiz.QuizPostRequest;
import com.example.didyouknow.dto.quiz.QuizPostResponse;
import com.example.didyouknow.service.FileStorageService;
import com.example.didyouknow.service.QuizPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizPostService quizPostService;
    private final FileStorageService fileStorageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<QuizPostResponse> create(
            @RequestParam Long userId,
            @RequestPart("quiz") QuizPostRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        
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
        
        QuizPostResponse response = quizPostService.create(userId, request, imageUrls);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<QuizPostResponse>> findAll() {
        return ResponseEntity.ok(quizPostService.findAll());
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizPostResponse> findById(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizPostService.findById(quizId));
    }

    @DeleteMapping("/{quizId}")
    public ResponseEntity<Void> delete(@PathVariable Long quizId) {
        quizPostService.delete(quizId);
        return ResponseEntity.noContent().build();
    }
}
