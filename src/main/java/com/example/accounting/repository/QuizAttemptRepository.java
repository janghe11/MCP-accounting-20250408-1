package com.example.accounting.repository;

import com.example.accounting.model.Quiz;
import com.example.accounting.model.QuizAttempt;
import com.example.accounting.model.Subject;
import com.example.accounting.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByUser(User user);
    List<QuizAttempt> findByUserAndQuiz(User user, Quiz quiz);
    List<QuizAttempt> findByUserAndIsCorrect(User user, boolean isCorrect);
    
    @Query("SELECT qa FROM QuizAttempt qa WHERE qa.user = ?1 AND qa.quiz.subject = ?2")
    List<QuizAttempt> findByUserAndSubject(User user, Subject subject);
    
    @Query("SELECT COUNT(qa) FROM QuizAttempt qa WHERE qa.user = ?1 AND qa.quiz.subject = ?2 AND qa.isCorrect = true")
    Long countCorrectQuizzesByUserAndSubject(User user, Subject subject);
    
    @Query("SELECT COUNT(qa) FROM QuizAttempt qa WHERE qa.user = ?1 AND qa.quiz.subject = ?2")
    Long countTotalQuizzesByUserAndSubject(User user, Subject subject);
}
