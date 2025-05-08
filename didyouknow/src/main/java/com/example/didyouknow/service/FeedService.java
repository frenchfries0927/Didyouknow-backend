package com.example.didyouknow.service;

import com.example.didyouknow.domain.KnowledgePost;
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

        // 1. KnowledgePost -> FeedResponse
        List<KnowledgePost> knowledgePosts = knowledgePostRepository.findAll();
        for (KnowledgePost post : knowledgePosts) {
            feeds.add(new FeedResponse(
                    post.getId(),
                    "knowledge",
                    post.getTitle(),
                    post.getContent(),
                    null, // imageUrl 없음
                    post.getAuthor().getNickname(),
                    post.getAuthor().getProfileImageUrl(),
                    post.getCreatedAt()
            ));
        }

        // 2. QuizPost -> FeedResponse
        List<QuizPost> quizPosts = quizPostRepository.findAll();
        for (QuizPost post : quizPosts) {
            feeds.add(new FeedResponse(
                    post.getId(),
                    "quiz",
                    post.getQuestion(),
                    "정답: 옵션 " + post.getCorrectOption(),  // 또는 빈 문자열
                    post.getImageUrl(),
                    post.getAuthor().getNickname(),
                    post.getAuthor().getProfileImageUrl(),
                    post.getCreatedAt()
            ));
        }

        // 최신순 정렬
        feeds.sort(Comparator.comparing(FeedResponse::getCreatedAt).reversed());

        return feeds;
    }
}
