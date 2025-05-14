package com.example.didyouknow.repository;

import com.example.didyouknow.domain.Bookmark;
import com.example.didyouknow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Bookmark
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByUserAndTargetTypeAndTargetId(User user, String targetType, Long targetId);
    List<Bookmark> findByUser(User user);

    boolean existsByUserAndTargetTypeAndTargetId(User user, String targetType, Long targetId);

    void deleteByUserAndTargetId(User user, Long targetId);


}
