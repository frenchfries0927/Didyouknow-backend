package com.example.didyouknow.repository;

import com.example.didyouknow.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    
    /**
     * 게시물 ID로 이미지를 찾아 표시 순서대로 정렬
     */
    List<PostImage> findByPostIdOrderBySequence(Long postId);
    
    /**
     * 게시물 ID로 이미지 삭제
     */
    @Modifying
    @Query("DELETE FROM PostImage pi WHERE pi.post.id = :postId")
    void deleteByPostId(Long postId);
} 