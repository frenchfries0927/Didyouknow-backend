package com.example.didyouknow.service;

import com.example.didyouknow.domain.KnowledgePost;
import com.example.didyouknow.domain.PostImage;
import com.example.didyouknow.domain.QuizPost;
import com.example.didyouknow.dto.feed.FeedResponse;
import com.example.didyouknow.repository.KnowledgePostRepository;
import com.example.didyouknow.repository.QuizPostRepository;
import com.example.didyouknow.repository.LikeRepository;
import com.example.didyouknow.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final KnowledgePostRepository knowledgePostRepository;
    private final QuizPostRepository quizPostRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    public List<FeedResponse> getFeed() {
        return getFeedForUser(null); // 비로그인 사용자용
    }
    
    public List<FeedResponse> getFeedForUser(Long userId) {
        List<FeedResponse> feeds = new ArrayList<>();

        // 1. KnowledgePost → FeedResponse
        List<KnowledgePost> knowledgePosts = knowledgePostRepository.findAll();
        for (KnowledgePost post : knowledgePosts) {
            // 이미지 URL 가져오기 (첫 번째 이미지만 사용)
            String imageUrl = null;
            if (post.getImages() != null && !post.getImages().isEmpty()) {
                // 이미지가 있으면 sequence 순으로 정렬하여 첫 번째 이미지 선택
                List<PostImage> sortedImages = post.getImages().stream()
                        .sorted(Comparator.comparing(PostImage::getSequence,
                                Comparator.nullsLast(Comparator.naturalOrder())))
                        .toList();

                if (!sortedImages.isEmpty()) {
                    imageUrl = sortedImages.get(0).getImageUrl();
                }
            }

            // 좋아요 및 댓글 개수 조회
            Long likesCount = likeRepository.countByTargetTypeAndTargetId("knowledge", post.getId());
            Long commentsCount = commentRepository.countByTargetTypeAndTargetId("knowledge", post.getId());
            Boolean isLiked = userId != null ? likeRepository.isLikedByUser(userId, "knowledge", post.getId()) : false;

            feeds.add(new FeedResponse(
                    post.getId(),
                    "knowledge",
                    post.getTitle(),
                    post.getContent(),
                    imageUrl,  // 첫 번째 이미지 URL 사용
                    post.getAuthor().getId(),
                    post.getAuthor().getNickname(),
                    post.getAuthor().getProfileImageUrl(),
                    post.getCreatedAt(),
                    null,  // options 없음
                    likesCount,
                    commentsCount,
                    isLiked
            ));
        }

        // 2. QuizPost → FeedResponse
        List<QuizPost> quizPosts = quizPostRepository.findAll();
        for (QuizPost post : quizPosts) {
            List<String> options = List.of(
                    post.getOption1(),
                    post.getOption2(),
                    post.getOption3(),
                    post.getOption4()
            );

            // 좋아요 및 댓글 개수 조회
            Long likesCount = likeRepository.countByTargetTypeAndTargetId("quiz", post.getId());
            Long commentsCount = commentRepository.countByTargetTypeAndTargetId("quiz", post.getId());
            Boolean isLiked = userId != null ? likeRepository.isLikedByUser(userId, "quiz", post.getId()) : false;

            feeds.add(new FeedResponse(
                    post.getId(),
                    "quiz",
                    post.getQuestion(),
                    "정답: 옵션 " + post.getCorrectOption(),
                    post.getImageUrl(),
                    post.getAuthor().getId(),
                    post.getAuthor().getNickname(),
                    post.getAuthor().getProfileImageUrl(),
                    post.getCreatedAt(),
                    options,
                    likesCount,
                    commentsCount,
                    isLiked
            ));
        }

        // 최신순 정렬
        feeds.sort(Comparator.comparing(FeedResponse::getCreatedAt).reversed());
        return feeds;
    }

    public Slice<FeedResponse> getPopularFeeds(Pageable pageable, Long userId) {
        int limit = pageable.getPageSize();
        int offset = pageable.getPageNumber() * limit;
        int minLikes = 2;

        List<FeedResponse> allFeeds = new ArrayList<>();

        // 1. KnowledgePost - 좋아요 30개 이상
        List<KnowledgePost> knowledgePosts = knowledgePostRepository
                .findPopularPostsAboveLikeThreshold(minLikes, limit, offset);

        for (KnowledgePost post : knowledgePosts) {
            String imageUrl = post.getImages() != null && !post.getImages().isEmpty()
                    ? post.getImages().stream()
                    .sorted(Comparator.comparing(PostImage::getSequence, Comparator.nullsLast(Comparator.naturalOrder())))
                    .map(PostImage::getImageUrl)
                    .findFirst().orElse(null)
                    : null;

            Long likesCount = likeRepository.countByTargetTypeAndTargetId("knowledge", post.getId());
            Long commentsCount = commentRepository.countByTargetTypeAndTargetId("knowledge", post.getId());
            Boolean isLiked = userId != null && likeRepository.isLikedByUser(userId, "knowledge", post.getId());

            allFeeds.add(new FeedResponse(
                    post.getId(), "knowledge", post.getTitle(), post.getContent(), imageUrl,
                    post.getAuthor().getId(), post.getAuthor().getNickname(), post.getAuthor().getProfileImageUrl(),
                    post.getCreatedAt(), null, likesCount, commentsCount, isLiked
            ));
        }

        // 2. QuizPost - 좋아요 30개 이상
        List<QuizPost> quizPosts = quizPostRepository
                .findPopularPostsAboveLikeThreshold(minLikes, limit, offset);

        for (QuizPost post : quizPosts) {
            List<String> options = List.of(post.getOption1(), post.getOption2(), post.getOption3(), post.getOption4());

            Long likesCount = likeRepository.countByTargetTypeAndTargetId("quiz", post.getId());
            Long commentsCount = commentRepository.countByTargetTypeAndTargetId("quiz", post.getId());
            Boolean isLiked = userId != null && likeRepository.isLikedByUser(userId, "quiz", post.getId());

            allFeeds.add(new FeedResponse(
                    post.getId(), "quiz", post.getQuestion(), "정답: 옵션 " + post.getCorrectOption(), post.getImageUrl(),
                    post.getAuthor().getId(), post.getAuthor().getNickname(), post.getAuthor().getProfileImageUrl(),
                    post.getCreatedAt(), options, likesCount, commentsCount, isLiked
            ));
        }

        // 3. 정렬: 좋아요 기준
        allFeeds.sort(Comparator.comparing(FeedResponse::getLikes).reversed());

        // 4. Slice 처리
        boolean hasNext = allFeeds.size() > limit;
        List<FeedResponse> content = allFeeds.subList(0, Math.min(limit, allFeeds.size()));

        return new SliceImpl<>(content, pageable, hasNext);
    }
}