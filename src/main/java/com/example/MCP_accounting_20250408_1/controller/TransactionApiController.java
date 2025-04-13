package com.example.MCP_accounting_20250408_1.controller;

import com.example.MCP_accounting_20250408_1.model.Account;
import com.example.MCP_accounting_20250408_1.model.Transaction;
import com.example.MCP_accounting_20250408_1.service.AccountService;
import com.example.MCP_accounting_20250408_1.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionApiController {
    
    private final TransactionService transactionService;
    private final AccountService accountService;
    
    @Autowired
    public TransactionApiController(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }
    
    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionById(id);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/by-date-range")
    public List<Transaction> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return transactionService.getTransactionsByDateRange(startDate, endDate);
    }
    
    @GetMapping("/by-account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccount(@PathVariable Long accountId) {
        Account account = accountService.getAccountById(accountId);
        if (account != null) {
            List<Transaction> transactions = transactionService.getTransactionsByAccount(account);
            return ResponseEntity.ok(transactions);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        // 계정 존재 여부 확인
        Account debitAccount = accountService.getAccountById(transaction.getDebitAccount().getId());
        Account creditAccount = accountService.getAccountById(transaction.getCreditAccount().getId());
        
        if (debitAccount == null || creditAccount == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // 트랜잭션에 실제 계정 객체 설정
        transaction.setDebitAccount(debitAccount);
        transaction.setCreditAccount(creditAccount);
        
        Transaction savedTransaction = transactionService.saveTransaction(transaction);
        return ResponseEntity.ok(savedTransaction);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody Transaction transaction) {
        Transaction existingTransaction = transactionService.getTransactionById(id);
        if (existingTransaction == null) {
            return ResponseEntity.notFound().build();
        }
        
        // 계정 존재 여부 확인
        Account debitAccount = accountService.getAccountById(transaction.getDebitAccount().getId());
        Account creditAccount = accountService.getAccountById(transaction.getCreditAccount().getId());
        
        if (debitAccount == null || creditAccount == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // 트랜잭션에 실제 계정 객체 설정
        transaction.setId(id);
        transaction.setDebitAccount(debitAccount);
        transaction.setCreditAccount(creditAccount);
        
        Transaction updatedTransaction = transactionService.saveTransaction(transaction);
        return ResponseEntity.ok(updatedTransaction);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        Transaction existingTransaction = transactionService.getTransactionById(id);
        if (existingTransaction != null) {
            transactionService.deleteTransaction(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
