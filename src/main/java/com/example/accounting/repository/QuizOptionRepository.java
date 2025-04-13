package com.example.accounting.repository;

import com.example.accounting.model.Quiz;
import com.example.accounting.model.QuizOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizOptionRepository extends JpaRepository<QuizOption, Long> {
    List<QuizOption> findByQuiz(Quiz quiz);
    List<QuizOption> findByQuizOrderByOrderIndexAsc(Quiz quiz);
    Optional<QuizOption> findByQuizAndIsCorrect(Quiz quiz, boolean isCorrect);
}
