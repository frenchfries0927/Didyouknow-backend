package com.example.didyouknow.service;

import com.example.didyouknow.domain.KnowledgePost;
import com.example.didyouknow.domain.PostImage;
import com.example.didyouknow.domain.User;
import com.example.didyouknow.dto.post.KnowledgePostRequest;
import com.example.didyouknow.dto.post.KnowledgePostResponse;
import com.example.didyouknow.repository.KnowledgePostRepository;
import com.example.didyouknow.repository.UserRepository;
import com.example.didyouknow.repository.LikeRepository;
import com.example.didyouknow.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KnowledgePostService {

    private final KnowledgePostRepository knowledgePostRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public KnowledgePostResponse create(Long authorId, KnowledgePostRequest request, List<String> imageUrls) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

        KnowledgePost post = new KnowledgePost();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setPublishDate(LocalDate.parse(request.getPublishDate()));
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        // 이미지 처리
        if (imageUrls != null && !imageUrls.isEmpty()) {
            List<PostImage> images = new ArrayList<>();
            for (int i = 0; i < imageUrls.size(); i++) {
                PostImage image = PostImage.builder()
                    .imageUrl(imageUrls.get(i))
                    .sequence(i)
                    .createdAt(LocalDateTime.now())
                    .post(post)
                    .build();
                images.add(image);
            }
            post.setImages(images);
        }

        KnowledgePost saved = knowledgePostRepository.save(post);

        return convertToResponse(saved);
    }

    public List<KnowledgePostResponse> findAll() {
        return knowledgePostRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public KnowledgePostResponse findById(Long postId) {
        KnowledgePost post = knowledgePostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return convertToResponse(post);
    }

    public void delete(Long postId) {
        knowledgePostRepository.deleteById(postId);
    }

    @Transactional
    public void deleteByUserIdAndPostId(Long userId, Long postId) {
        KnowledgePost post = knowledgePostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        
        if (!post.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("게시글을 삭제할 권한이 없습니다.");
        }
        
        knowledgePostRepository.deleteById(postId);
    }

    private KnowledgePostResponse convertToResponse(KnowledgePost post) {
        List<String> imageUrls = post.getImages().stream()
                .sorted(Comparator.comparing(PostImage::getSequence))
                .map(PostImage::getImageUrl)
                .collect(Collectors.toList());

        // 좋아요와 댓글 정보 조회
        Long likesCount = likeRepository.countByTargetTypeAndTargetId("knowledge", post.getId());
        Long commentsCount = commentRepository.countByTargetTypeAndTargetId("knowledge", post.getId());

        return new KnowledgePostResponse(
                post.getId(),
                "knowledge",
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getNickname(),
                post.getPublishDate().toString(),
                imageUrls,
                likesCount,
                commentsCount,
                false // 기본값으로 false 설정
        );
    }
}
