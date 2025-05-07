package com.example.didyouknow.service;

import com.example.didyouknow.domain.KnowledgePost;
import com.example.didyouknow.domain.User;
import com.example.didyouknow.dto.post.KnowledgePostRequest;
import com.example.didyouknow.dto.post.KnowledgePostResponse;
import com.example.didyouknow.repository.KnowledgePostRepository;
import com.example.didyouknow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KnowledgePostService {

    private final KnowledgePostRepository knowledgePostRepository;
    private final UserRepository userRepository;

    public KnowledgePostResponse create(Long authorId, KnowledgePostRequest request) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("작성자 없음"));

        KnowledgePost post = new KnowledgePost();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setPublishDate(LocalDate.parse(request.getPublishDate()));
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        KnowledgePost saved = knowledgePostRepository.save(post);

        return new KnowledgePostResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getContent(),
                saved.getAuthor().getNickname(),
                saved.getPublishDate().toString()
        );
    }

    public KnowledgePostResponse findById(Long id) {
        KnowledgePost post = knowledgePostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("포스트 없음"));

        return new KnowledgePostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getNickname(),
                post.getPublishDate().toString()
        );
    }

    public List<KnowledgePostResponse> findAll() {
        return knowledgePostRepository.findAll().stream()
                .map(post -> new KnowledgePostResponse(
                        post.getId(), post.getTitle(), post.getContent(),
                        post.getAuthor().getNickname(), post.getPublishDate().toString()))
                .toList();
    }

    public void delete(Long id) {
        knowledgePostRepository.deleteById(id);
    }
}
