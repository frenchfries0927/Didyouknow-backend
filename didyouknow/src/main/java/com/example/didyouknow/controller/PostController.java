package com.example.didyouknow.controller;

import com.example.didyouknow.dto.post.KnowledgePostRequest;
import com.example.didyouknow.dto.post.KnowledgePostResponse;
import com.example.didyouknow.service.KnowledgePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final KnowledgePostService knowledgePostService;

    @PostMapping
    public ResponseEntity<KnowledgePostResponse> create(@RequestParam Long userId,
                                                        @RequestBody KnowledgePostRequest request) {
        KnowledgePostResponse response = knowledgePostService.create(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<KnowledgePostResponse>> findAll() {
        return ResponseEntity.ok(knowledgePostService.findAll());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<KnowledgePostResponse> findById(@PathVariable Long postId) {
        return ResponseEntity.ok(knowledgePostService.findById(postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable Long postId) {
        knowledgePostService.delete(postId);
        return ResponseEntity.noContent().build();
    }
}
