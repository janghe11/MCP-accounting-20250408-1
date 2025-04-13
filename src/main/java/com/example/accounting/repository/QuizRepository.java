package com.example.accounting.repository;

import com.example.accounting.model.Quiz;
import com.example.accounting.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findBySubject(Subject subject);
    List<Quiz> findByType(Quiz.QuizType type);
    List<Quiz> findByDifficulty(Integer difficulty);
    List<Quiz> findBySubjectAndDifficulty(Subject subject, Integer difficulty);
    List<Quiz> findBySubjectAndType(Subject subject, Quiz.QuizType type);
}
