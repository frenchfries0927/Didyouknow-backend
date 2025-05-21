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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            @RequestParam("userId") Long userId,
            @RequestParam("question") String question,
            @RequestParam("option1") String option1,
            @RequestParam("option2") String option2,
            @RequestParam("option3") String option3,
            @RequestParam("option4") String option4,
            @RequestParam("correctOption") int correctOption,
            @RequestParam("publishDate") String publishDate,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        
        // QuizPostRequest 객체 생성
        QuizPostRequest request = new QuizPostRequest();
        request.setQuestion(question);
        request.setOption1(option1);
        request.setOption2(option2);
        request.setOption3(option3);
        request.setOption4(option4);
        request.setCorrectOption(correctOption);
        request.setPublishDate(publishDate);
        
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
