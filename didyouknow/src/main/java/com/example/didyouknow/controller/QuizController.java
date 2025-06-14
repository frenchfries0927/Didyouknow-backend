package com.example.didyouknow.controller;

import com.example.didyouknow.auth.CustomUserDetails;
import com.example.didyouknow.common.ApiResponse;
import com.example.didyouknow.common.ApiResponseHelper;
import com.example.didyouknow.common.ResponseCode;
import com.example.didyouknow.dto.quiz.QuizPostRequest;
import com.example.didyouknow.dto.quiz.QuizPostResponse;
import com.example.didyouknow.service.FileStorageService;
import com.example.didyouknow.service.QuizPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<ApiResponse<QuizPostResponse>> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("question") String question,
            @RequestParam("option1") String option1,
            @RequestParam("option2") String option2,
            @RequestParam("option3") String option3,
            @RequestParam("option4") String option4,
            @RequestParam("correctOption") int correctOption,
            @RequestParam("publishDate") String publishDate,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        try {
            QuizPostRequest request = new QuizPostRequest();
            request.setQuestion(question);
            request.setOption1(option1);
            request.setOption2(option2);
            request.setOption3(option3);
            request.setOption4(option4);
            request.setCorrectOption(correctOption);
            request.setPublishDate(publishDate);

            List<String> imageUrls = new ArrayList<>();
            if (images != null && !images.isEmpty()) {
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        String imageUrl = fileStorageService.storeFile(image);
                        imageUrls.add(imageUrl);
                    }
                }
            }
            long userId = userDetails.getUserId();
            QuizPostResponse response = quizPostService.create(userId, request, imageUrls);
            return ApiResponseHelper.success(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponseHelper.error(ResponseCode.SERVER_ERROR, "퀴즈 생성 중 오류 발생: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<QuizPostResponse>>> findAll() {
        return ApiResponseHelper.success(quizPostService.findAll());
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<ApiResponse<QuizPostResponse>> findById(@PathVariable("quizId") Long quizId) {
        QuizPostResponse quiz = quizPostService.findById(quizId);
        return ApiResponseHelper.success(quiz);
    }

    @DeleteMapping("/{quizId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("quizId") Long quizId) {
        quizPostService.delete(quizId);
        return ApiResponseHelper.success(null);
    }
}
