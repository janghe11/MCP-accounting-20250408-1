package com.example.accounting.repository;

import com.example.accounting.model.Content;
import com.example.accounting.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findBySubject(Subject subject);
    List<Content> findBySubjectOrderByOrderIndexAsc(Subject subject);
    List<Content> findByType(Content.ContentType type);
    List<Content> findBySubjectAndType(Subject subject, Content.ContentType type);
}
