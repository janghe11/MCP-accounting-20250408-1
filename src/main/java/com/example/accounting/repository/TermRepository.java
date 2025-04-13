package com.example.accounting.repository;

import com.example.accounting.model.Subject;
import com.example.accounting.model.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {
    Optional<Term> findByTerm(String term);
    List<Term> findByTermContainingIgnoreCase(String keyword);
    List<Term> findBySubject(Subject subject);
}
