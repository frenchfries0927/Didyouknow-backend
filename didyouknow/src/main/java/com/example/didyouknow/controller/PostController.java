package com.example.didyouknow.controller;



import com.example.didyouknow.dto.PostRequest;
import com.example.didyouknow.dto.PostResponse;
import com.example.didyouknow.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest) {
        postService.create(postRequest);
        return ResponseEntity.ok("작성 완료");
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> listPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable UUID postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable UUID postId, @RequestBody PostRequest req) {
        postService.updatePost(postId, req);
        return ResponseEntity.ok("수정 완료");
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable UUID postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok("삭제 완료");
    }
}
