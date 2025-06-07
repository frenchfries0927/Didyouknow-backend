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
import java.util.stream.Collectors;

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
                saved.getUser().getId(),
                saved.getUser().getProfileImageUrl() != null ? saved.getUser().getProfileImageUrl() : "",
                saved.getCreatedAt().toString(),
                saved.getParentComment() != null ? saved.getParentComment().getId() : null,
                0,
                List.of()
        );
    }

    public List<CommentResponse> findByTarget(String targetType, Long targetId) {
        List<Comment> allComments = commentRepository.findByTargetTypeAndTargetId(targetType, targetId);
        
        // 최상위 댓글만 필터링 (parentComment가 null인 것들)
        List<Comment> topLevelComments = allComments.stream()
                .filter(comment -> comment.getParentComment() == null)
                .collect(Collectors.toList());
        
        // 각 최상위 댓글에 대해 대댓글을 찾아서 포함
        return topLevelComments.stream()
                .map(comment -> buildCommentResponse(comment, allComments))
                .collect(Collectors.toList());
    }
    
    private CommentResponse buildCommentResponse(Comment comment, List<Comment> allComments) {
        // 현재 댓글의 대댓글들을 찾기
        List<CommentResponse> replies = allComments.stream()
                .filter(c -> c.getParentComment() != null && c.getParentComment().getId().equals(comment.getId()))
                .map(reply -> new CommentResponse(
                        reply.getId(),
                        reply.getContent(),
                        reply.getUser().getNickname(),
                        reply.getUser().getId(),
                        reply.getUser().getProfileImageUrl() != null ? reply.getUser().getProfileImageUrl() : "",
                        reply.getCreatedAt().toString(),
                        reply.getParentComment().getId(),
                        0,
                        List.of()
                ))
                .collect(Collectors.toList());
        
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getNickname(),
                comment.getUser().getId(),
                comment.getUser().getProfileImageUrl() != null ? comment.getUser().getProfileImageUrl() : "",
                comment.getCreatedAt().toString(),
                comment.getParentComment() != null ? comment.getParentComment().getId() : null,
                0,
                replies
        );
    }

    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
    
    public Long getCommentCount(String targetType, Long targetId) {
        return commentRepository.countByTargetTypeAndTargetId(targetType, targetId);
    }
}
