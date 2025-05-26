package com.example.didyouknow.repository;

import com.example.didyouknow.domain.KnowledgePost;
import com.example.didyouknow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// KnowledgePost
public interface KnowledgePostRepository extends JpaRepository<KnowledgePost, Long> {
    List<KnowledgePost> findByAuthorOrderByCreatedAtDesc(User author);
}
