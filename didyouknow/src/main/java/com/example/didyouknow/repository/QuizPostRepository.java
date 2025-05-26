package com.example.didyouknow.repository;

import com.example.didyouknow.domain.QuizPost;
import com.example.didyouknow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// QuizPost
public interface QuizPostRepository extends JpaRepository<QuizPost, Long> {
    List<QuizPost> findByAuthorOrderByCreatedAtDesc(User author);
}
