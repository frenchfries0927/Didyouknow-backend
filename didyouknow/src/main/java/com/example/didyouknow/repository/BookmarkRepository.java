package com.example.didyouknow.repository;

import com.example.didyouknow.domain.Bookmark;
import com.example.didyouknow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// Bookmark
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByUserAndTargetTypeAndTargetId(User user, String targetType, Long targetId);
    List<Bookmark> findByUser(User user);

    boolean existsByUserAndTargetTypeAndTargetId(User user, String targetType, Long targetId);

    @Modifying
    @Query("DELETE FROM Bookmark b WHERE b.user = :user AND b.targetType = :targetType AND b.targetId = :targetId")
    void deleteByUserAndTargetTypeAndTargetId(@Param("user") User user, @Param("targetType") String targetType, @Param("targetId") Long targetId);

    void deleteByUserAndTargetId(User user, Long targetId);
}
