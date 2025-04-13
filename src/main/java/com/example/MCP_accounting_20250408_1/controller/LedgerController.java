package com.example.MCP_accounting_20250408_1.controller;

import com.example.MCP_accounting_20250408_1.model.Account;
import com.example.MCP_accounting_20250408_1.model.Transaction;
import com.example.MCP_accounting_20250408_1.service.AccountService;
import com.example.MCP_accounting_20250408_1.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ledger")
public class LedgerController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @Autowired
    public LedgerController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping
    public String viewGeneralLedger(Model model) {
        List<Account> accounts = accountService.getAllAccounts();
        Map<String, BigDecimal> accountBalances = transactionService.calculateAccountBalances();
        
        model.addAttribute("accounts", accounts);
        model.addAttribute("accountBalances", accountBalances);
        model.addAttribute("title", "종합 원장");
        model.addAttribute("content", "모든 계정과 해당 잔액을 확인합니다.");
        
        return "ledger";
    }

    @GetMapping("/{id}")
    public String viewAccountLedger(@PathVariable Long id, Model model) {
        Account account = accountService.getAccountById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account ID: " + id));
        
        List<Transaction> transactions = transactionService.getTransactionsByAccount(account);
        BigDecimal balance = BigDecimal.ZERO;
        Map<Transaction, BigDecimal> runningBalances = new HashMap<>();
        
        for (Transaction transaction : transactions) {
            if (transaction.getDebitAccount().equals(account)) {
                // 자산, 비용 계정은 차변 증가, 부채, 자본, 수익 계정은 차변 감소
                if ("자산".equals(account.getAccountType()) || "비용".equals(account.getAccountType())) {
                    balance = balance.add(transaction.getAmount());
                } else {
                    balance = balance.subtract(transaction.getAmount());
                }
            } else if (transaction.getCreditAccount().equals(account)) {
                // 부채, 자본, 수익 계정은 대변 증가, 자산, 비용 계정은 대변 감소
                if ("부채".equals(account.getAccountType()) || "자본".equals(account.getAccountType()) || "수익".equals(account.getAccountType())) {
                    balance = balance.add(transaction.getAmount());
                } else {
                    balance = balance.subtract(transaction.getAmount());
                }
            }
            runningBalances.put(transaction, balance);
        }
        
        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);
        model.addAttribute("runningBalances", runningBalances);
        model.addAttribute("finalBalance", balance);
        model.addAttribute("title", account.getAccountName() + " 원장");
        
        return "ledger-account";
    }
}
