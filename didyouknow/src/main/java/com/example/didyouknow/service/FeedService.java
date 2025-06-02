package com.example.didyouknow.service;

import com.example.didyouknow.domain.KnowledgePost;
import com.example.didyouknow.domain.PostImage;
import com.example.didyouknow.domain.QuizPost;
import com.example.didyouknow.dto.feed.FeedResponse;
import com.example.didyouknow.repository.KnowledgePostRepository;
import com.example.didyouknow.repository.QuizPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final KnowledgePostRepository knowledgePostRepository;
    private final QuizPostRepository quizPostRepository;

    public List<FeedResponse> getFeed() {
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
                    null  // options 없음
            ));
        }

        // 2. QuizPost → FeedResponse (변경 없음)
        List<QuizPost> quizPosts = quizPostRepository.findAll();
        for (QuizPost post : quizPosts) {
            List<String> options = List.of(
                    post.getOption1(),
                    post.getOption2(),
                    post.getOption3(),
                    post.getOption4()
            );

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
                    options
            ));
        }

        // 최신순 정렬
        feeds.sort(Comparator.comparing(FeedResponse::getCreatedAt).reversed());
        return feeds;
    }
}