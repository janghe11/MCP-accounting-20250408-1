package com.example.MCP_accounting_20250408_1.service;

import com.example.MCP_accounting_20250408_1.model.Account;
import com.example.MCP_accounting_20250408_1.model.Transaction;
import com.example.MCP_accounting_20250408_1.repository.AccountRepository;
import com.example.MCP_accounting_20250408_1.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    /**
     * 모든 거래 내역 조회
     */
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
    
    /**
     * 특정 기간 내 거래 내역 조회
     */
    public List<Transaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByTransactionDateBetween(startDate, endDate);
    }
    
    /**
     * 특정 계정 관련 거래 내역 조회
     */
    public List<Transaction> getTransactionsByAccount(Long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isPresent()) {
            return transactionRepository.findByDebitAccountOrCreditAccount(account.get(), account.get());
        }
        return List.of();
    }
    
    /**
     * 거래 ID로 거래 조회
     */
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }
    
    /**
     * 새 거래 생성
     */
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
    
    /**
     * 거래 내역 수정
     */
    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            Transaction existingTransaction = transaction.get();
            existingTransaction.setTransactionDate(transactionDetails.getTransactionDate());
            existingTransaction.setDescription(transactionDetails.getDescription());
            existingTransaction.setDebitAccount(transactionDetails.getDebitAccount());
            existingTransaction.setCreditAccount(transactionDetails.getCreditAccount());
            existingTransaction.setAmount(transactionDetails.getAmount());
            existingTransaction.setReference(transactionDetails.getReference());
            return transactionRepository.save(existingTransaction);
        }
        return null;
    }
    
    /**
     * 거래 내역 삭제
     */
    public boolean deleteTransaction(Long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            transactionRepository.delete(transaction.get());
            return true;
        }
        return false;
    }
    
    /**
     * 계정 코드 또는 이름으로 계정 조회
     */
    public Account findAccountByName(String accountName) {
        return accountRepository.findByAccountName(accountName);
    }
}