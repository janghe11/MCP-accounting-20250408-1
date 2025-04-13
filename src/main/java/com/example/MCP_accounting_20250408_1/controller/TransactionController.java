package com.example.MCP_accounting_20250408_1.controller;

import com.example.MCP_accounting_20250408_1.model.Account;
import com.example.MCP_accounting_20250408_1.model.Transaction;
import com.example.MCP_accounting_20250408_1.service.AccountService;
import com.example.MCP_accounting_20250408_1.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private AccountService accountService;
    
    @GetMapping
    public String getAllTransactions(Model model) {
        List<Transaction> transactions = transactionService.getAllTransactions();
        List<Account> accounts = accountService.getAllAccounts();
        
        model.addAttribute("transactions", transactions);
        model.addAttribute("accounts", accounts);
        model.addAttribute("transaction", new Transaction());  // 신규 거래 생성용
        model.addAttribute("title", "거래 내역");
        
        return "transactions/list";
    }
    
    @GetMapping("/filter")
    public String getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {
        
        List<Transaction> transactions = transactionService.getTransactionsByDateRange(startDate, endDate);
        model.addAttribute("transactions", transactions);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("title", "거래 내역 조회");
        
        return "transactions/filtered";
    }
    
    @GetMapping("/account/{accountId}")
    public String getTransactionsByAccount(@PathVariable Long accountId, Model model) {
        Account account = accountService.getAccountById(accountId);
        List<Transaction> transactions = transactionService.getTransactionsByAccount(accountId);
        
        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);
        model.addAttribute("title", account.getAccountName() + " 계정 거래 내역");
        
        return "transactions/by-account";
    }
    
    @GetMapping("/{id}")
    public String getTransactionDetails(@PathVariable Long id, Model model) {
        Transaction transaction = transactionService.getTransactionById(id);
        
        model.addAttribute("transaction", transaction);
        model.addAttribute("title", "거래 상세 정보");
        
        return "transactions/detail";
    }
    
    @PostMapping
    public String createTransaction(@ModelAttribute Transaction transaction) {
        transactionService.createTransaction(transaction);
        return "redirect:/transactions";
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Transaction transaction = transactionService.getTransactionById(id);
        List<Account> accounts = accountService.getAllAccounts();
        
        model.addAttribute("transaction", transaction);
        model.addAttribute("accounts", accounts);
        model.addAttribute("title", "거래 정보 수정");
        
        return "transactions/edit";
    }
    
    @PostMapping("/{id}")
    public String updateTransaction(@PathVariable Long id, @ModelAttribute Transaction transaction) {
        transactionService.updateTransaction(id, transaction);
        return "redirect:/transactions";
    }
    
    @GetMapping("/{id}/delete")
    public String deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return "redirect:/transactions";
    }
}