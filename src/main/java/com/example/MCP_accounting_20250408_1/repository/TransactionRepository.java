package com.example.MCP_accounting_20250408_1.repository;

import com.example.MCP_accounting_20250408_1.model.Account;
import com.example.MCP_accounting_20250408_1.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);
    List<Transaction> findByDebitAccountOrCreditAccount(Account debitAccount, Account creditAccount);
}
