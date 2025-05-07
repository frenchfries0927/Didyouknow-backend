package com.example.didyouknow.service;

import com.example.didyouknow.domain.Comment;
import com.example.didyouknow.domain.User;
import com.example.didyouknow.dto.comment.CommentRequest;
import com.example.didyouknow.dto.comment.CommentResponse;
import com.example.didyouknow.repository.CommentRepository;
import com.example.didyouknow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentResponse create(Long userId, CommentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setTargetType(request.getTargetType());
        comment.setTargetId(request.getTargetId());
        comment.setContent(request.getContent());
        comment.setCreatedAt(LocalDateTime.now());

        if (request.getParentCommentId() != null) {
            comment.setParentComment(
                    commentRepository.findById(request.getParentCommentId()).orElse(null)
            );
        }

        Comment saved = commentRepository.save(comment);

        return new CommentResponse(
                saved.getId(),
                saved.getContent(),
                saved.getUser().getNickname(),
                saved.getCreatedAt().toString()
        );
    }

    public List<CommentResponse> findByTarget(String targetType, Long targetId) {
        return commentRepository.findByTargetTypeAndTargetId(targetType, targetId).stream()
                .map(c -> new CommentResponse(
                        c.getId(),
                        c.getContent(),
                        c.getUser().getNickname(),
                        c.getCreatedAt().toString()
                ))
                .toList();
    }

    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
