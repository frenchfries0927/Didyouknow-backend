package com.example.didyouknow.service;

import com.example.didyouknow.domain.QuizPost;
import com.example.didyouknow.domain.User;
import com.example.didyouknow.dto.quiz.QuizPostRequest;
import com.example.didyouknow.dto.quiz.QuizPostResponse;
import com.example.didyouknow.repository.QuizPostRepository;
import com.example.didyouknow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizPostService {

    private final QuizPostRepository quizPostRepository;
    private final UserRepository userRepository;

    public QuizPostResponse create(Long authorId, QuizPostRequest request) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("작성자 없음"));

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

        QuizPost saved = quizPostRepository.save(quiz);

        return new QuizPostResponse(
                saved.getId(),
                saved.getQuestion(),
                new String[]{saved.getOption1(), saved.getOption2(), saved.getOption3(), saved.getOption4()},
                saved.getAuthor().getNickname(),
                saved.getPublishDate().toString()
        );
    }

    public QuizPostResponse findById(Long id) {
        QuizPost quiz = quizPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("퀴즈 없음"));

        return new QuizPostResponse(
                quiz.getId(),
                quiz.getQuestion(),
                new String[]{quiz.getOption1(), quiz.getOption2(), quiz.getOption3(), quiz.getOption4()},
                quiz.getAuthor().getNickname(),
                quiz.getPublishDate().toString()
        );
    }

    public List<QuizPostResponse> findAll() {
        return quizPostRepository.findAll().stream()
                .map(quiz -> new QuizPostResponse(
                        quiz.getId(), quiz.getQuestion(),
                        new String[]{quiz.getOption1(), quiz.getOption2(), quiz.getOption3(), quiz.getOption4()},
                        quiz.getAuthor().getNickname(),
                        quiz.getPublishDate().toString()
                )).toList();
    }

    public void delete(Long id) {
        quizPostRepository.deleteById(id);
    }
}
