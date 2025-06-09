package com.example.didyouknow.service;

import com.example.didyouknow.domain.QuizPost;
import com.example.didyouknow.domain.QuizPostImage;
import com.example.didyouknow.domain.User;
import com.example.didyouknow.dto.quiz.QuizPostRequest;
import com.example.didyouknow.dto.quiz.QuizPostResponse;
import com.example.didyouknow.repository.QuizPostRepository;
import com.example.didyouknow.repository.UserRepository;
import com.example.didyouknow.repository.LikeRepository;
import com.example.didyouknow.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizPostService {

    private final QuizPostRepository quizPostRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public QuizPostResponse create(Long authorId, QuizPostRequest request, List<String> imageUrls) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

        QuizPost quiz = new QuizPost();
        quiz.setQuestion(request.getQuestion());
        quiz.setOption1(request.getOption1());
        quiz.setOption2(request.getOption2());
        quiz.setOption3(request.getOption3());
        quiz.setOption4(request.getOption4());
        quiz.setCorrectOption(request.getCorrectOption());
        quiz.setPublishDate(LocalDate.parse(request.getPublishDate()));
        quiz.setAuthor(author);
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setUpdatedAt(LocalDateTime.now());

        // 첫 번째 이미지는 기존 imageUrl 필드에도 저장
        if (imageUrls != null && !imageUrls.isEmpty()) {
            quiz.setImageUrl(imageUrls.get(0));
            
            // 모든 이미지를 QuizPostImage 엔티티로 저장
            List<QuizPostImage> images = new ArrayList<>();
            for (int i = 0; i < imageUrls.size(); i++) {
                QuizPostImage image = QuizPostImage.builder()
                    .imageUrl(imageUrls.get(i))
                    .sequence(i)
                    .createdAt(LocalDateTime.now())
                    .quizPost(quiz)
                    .build();
                images.add(image);
            }
            quiz.setImages(images);
        }

        QuizPost saved = quizPostRepository.save(quiz);

        return convertToResponse(saved);
    }

    public List<QuizPostResponse> findAll() {
        return quizPostRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public QuizPostResponse findById(Long quizId) {
        QuizPost quiz = quizPostRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("퀴즈를 찾을 수 없습니다."));
        return convertToResponse(quiz);
    }

    public void delete(Long quizId) {
        quizPostRepository.deleteById(quizId);
    }

    @Transactional
    public void deleteByUserIdAndPostId(Long userId, Long postId) {
        QuizPost quiz = quizPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("퀴즈를 찾을 수 없습니다."));
        
        if (!quiz.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("퀴즈를 삭제할 권한이 없습니다.");
        }
        
        quizPostRepository.deleteById(postId);
    }

    private QuizPostResponse convertToResponse(QuizPost quiz) {
        List<String> imageUrls = quiz.getImages() != null ? 
            quiz.getImages().stream()
                .sorted(Comparator.comparing(QuizPostImage::getSequence))
                .map(QuizPostImage::getImageUrl)
                .collect(Collectors.toList()) : 
            new ArrayList<>();
        
        // 기존 imageUrl 필드가 있고 images 컬렉션이 비어있으면 기존 필드 사용
        if (imageUrls.isEmpty() && quiz.getImageUrl() != null) {
            imageUrls.add(quiz.getImageUrl());
        }

        // 좋아요와 댓글 정보 조회
        Long likesCount = likeRepository.countByTargetTypeAndTargetId("quiz", quiz.getId());
        Long commentsCount = commentRepository.countByTargetTypeAndTargetId("quiz", quiz.getId());

        return new QuizPostResponse(
                quiz.getId(),
                quiz.getQuestion(),
                new String[]{quiz.getOption1(), quiz.getOption2(), quiz.getOption3(), quiz.getOption4()},
                quiz.getAuthor().getNickname(),
                quiz.getPublishDate().toString(),
                imageUrls,
                likesCount,
                commentsCount,
                false // 기본값으로 false 설정
        );
    }
}
