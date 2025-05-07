package com.example.didyouknow.controller;

import com.example.didyouknow.dto.quiz.QuizPostRequest;
import com.example.didyouknow.dto.quiz.QuizPostResponse;
import com.example.didyouknow.service.QuizPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizPostService quizPostService;

    @PostMapping
    public ResponseEntity<QuizPostResponse> create(@RequestParam Long userId,
                                                   @RequestBody QuizPostRequest request) {
        QuizPostResponse response = quizPostService.create(userId, request);
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
