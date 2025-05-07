package com.example.didyouknow.service;

import com.example.didyouknow.domain.Bookmark;
import com.example.didyouknow.domain.User;
import com.example.didyouknow.dto.bookmark.BookmarkResponse;
import com.example.didyouknow.repository.BookmarkRepository;
import com.example.didyouknow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

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
}
