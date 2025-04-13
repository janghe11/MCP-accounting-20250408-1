package com.example.accounting.repository;

import com.example.accounting.model.Content;
import com.example.accounting.model.Progress;
import com.example.accounting.model.Subject;
import com.example.accounting.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByUser(User user);
    List<Progress> findByUserAndCompleted(User user, boolean completed);
    Optional<Progress> findByUserAndContent(User user, Content content);
    
    @Query("SELECT p FROM Progress p WHERE p.user = ?1 AND p.content.subject = ?2")
    List<Progress> findByUserAndSubject(User user, Subject subject);
    
    @Query("SELECT COUNT(p) FROM Progress p WHERE p.user = ?1 AND p.content.subject = ?2 AND p.completed = true")
    Long countCompletedContentsByUserAndSubject(User user, Subject subject);
    
    @Query("SELECT COUNT(p) FROM Progress p WHERE p.user = ?1 AND p.content.subject = ?2")
    Long countTotalContentsByUserAndSubject(User user, Subject subject);
}
