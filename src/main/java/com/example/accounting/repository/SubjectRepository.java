package com.example.accounting.repository;

import com.example.accounting.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByName(String name);
    List<Subject> findByCategory(String category);
    List<Subject> findByLevelLessThanEqual(Integer level);
}
