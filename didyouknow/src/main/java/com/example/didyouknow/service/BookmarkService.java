package com.example.didyouknow.service;

import com.example.didyouknow.domain.Bookmark;
import com.example.didyouknow.domain.User;
import com.example.didyouknow.dto.bookmark.BookmarkResponse;
import com.example.didyouknow.dto.post.KnowledgePostResponse;
import com.example.didyouknow.dto.quiz.QuizPostResponse;
import com.example.didyouknow.repository.BookmarkRepository;
import com.example.didyouknow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final KnowledgePostService knowledgePostService;
    private final QuizPostService quizPostService;

    public void addBookmark(Long userId, String targetType, Long targetId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        boolean exists = bookmarkRepository.existsByUserAndTargetTypeAndTargetId(user, targetType, targetId);
        if (!exists) {
            Bookmark bookmark = new Bookmark();
            bookmark.setUser(user);
            bookmark.setTargetType(targetType);
            bookmark.setTargetId(targetId);
            bookmark.setCreatedAt(LocalDateTime.now());
            bookmarkRepository.save(bookmark);
        }
    }

    public void removeBookmark(Long userId, Long targetId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        bookmarkRepository.deleteByUserAndTargetId(user, targetId);
    }

    public List<BookmarkResponse> getMyBookmarks(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        return bookmarkRepository.findByUser(user).stream()
                .map(b -> new BookmarkResponse(
                        b.getTargetType(),
                        b.getTargetId(),
                        b.getCreatedAt().toString()
                )).toList();
    }

    public List<Map<String, Object>> getBookmarkFeed(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        List<Bookmark> bookmarks = bookmarkRepository.findByUser(user);
        List<Map<String, Object>> feedItems = new ArrayList<>();

        for (Bookmark bookmark : bookmarks) {
            try {
                Map<String, Object> item = new HashMap<>();
                
                if ("knowledge".equals(bookmark.getTargetType())) {
                    KnowledgePostResponse post = knowledgePostService.findById(bookmark.getTargetId());
                    item.put("id", post.getId());
                    item.put("type", "knowledge");
                    item.put("title", post.getTitle());
                    item.put("content", post.getContent());
                    item.put("authorNickname", post.getAuthorNickname());
                    item.put("publishDate", post.getPublishDate());
                    item.put("imageUrls", post.getImageUrls());
                    item.put("likes", post.getLikes());
                    item.put("comments", post.getComments());
                    item.put("isLiked", post.getIsLiked());
                    item.put("isBookmarked", true);
                    item.put("bookmarkedAt", bookmark.getCreatedAt().toString());
                    feedItems.add(item);
                } else if ("quiz".equals(bookmark.getTargetType())) {
                    QuizPostResponse post = quizPostService.findById(bookmark.getTargetId());
                    item.put("id", post.getId());
                    item.put("type", "quiz");
                    item.put("title", post.getQuestion());
                    item.put("content", post.getQuestion());
                    item.put("authorNickname", post.getAuthorNickname());
                    item.put("publishDate", post.getPublishDate());
                    item.put("imageUrls", post.getImageUrls());
                    item.put("options", post.getOptions());
                    item.put("likes", post.getLikes());
                    item.put("comments", post.getComments());
                    item.put("isLiked", post.getIsLiked());
                    item.put("isBookmarked", true);
                    item.put("bookmarkedAt", bookmark.getCreatedAt().toString());
                    feedItems.add(item);
                }
            } catch (Exception e) {
                // 게시물이 삭제된 경우 스킵
                continue;
            }
        }

        // 북마크 생성 시간 기준으로 내림차순 정렬
        return feedItems.stream()
                .sorted((a, b) -> {
                    String timeA = (String) a.get("bookmarkedAt");
                    String timeB = (String) b.get("bookmarkedAt");
                    return timeB.compareTo(timeA);
                })
                .collect(Collectors.toList());
    }

    public boolean toggleBookmark(Long userId, String targetType, Long targetId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        boolean exists = bookmarkRepository.existsByUserAndTargetTypeAndTargetId(user, targetType, targetId);
        
        if (exists) {
            bookmarkRepository.deleteByUserAndTargetTypeAndTargetId(user, targetType, targetId);
            return false;
        } else {
            Bookmark bookmark = new Bookmark();
            bookmark.setUser(user);
            bookmark.setTargetType(targetType);
            bookmark.setTargetId(targetId);
            bookmark.setCreatedAt(LocalDateTime.now());
            bookmarkRepository.save(bookmark);
            return true;
        }
    }

    public boolean isBookmarked(Long userId, String targetType, Long targetId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        
        return bookmarkRepository.existsByUserAndTargetTypeAndTargetId(user, targetType, targetId);
    }
}
