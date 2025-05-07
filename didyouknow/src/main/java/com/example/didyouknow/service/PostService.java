package com.example.didyouknow.service;


import com.example.didyouknow.dto.PostRequest;
import com.example.didyouknow.dto.PostResponse;
import com.example.didyouknow.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void create(PostRequest req) {
        Post post = new Post();
        post.setTitle(req.getTitle());
        post.setContent(req.getContent());
        post.setPostType(req.getPostType());
        post.setSlidesJson(req.getSlidesJson());
        post.setCreatedAt(LocalDateTime.now());
        post.setAuthor(userRepository.findAll().get(0)); // mock 사용자 (임시)

        postRepository.save(post);
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(p -> new PostResponse(
                        p.getId(),
                        p.getTitle(),
                        p.getContent(),
                        p.getAuthor().getNickname(),
                        p.getPostType(),
                        p.getCreatedAt()
                )).toList();
    }

    public PostResponse getPost(UUID postId) {
        Post p = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        return new PostResponse(
                p.getId(), p.getTitle(), p.getContent(),
                p.getAuthor().getNickname(), p.getPostType(), p.getCreatedAt()
        );
    }

    public void updatePost(UUID postId, PostRequest req) {
        Post p = postRepository.findById(postId).orElseThrow();
        p.setTitle(req.getTitle());
        p.setContent(req.getContent());
        p.setSlidesJson(req.getSlidesJson());

        postRepository.save(p);
    }

    public void deletePost(UUID postId) {
        postRepository.deleteById(postId);
    }
}
