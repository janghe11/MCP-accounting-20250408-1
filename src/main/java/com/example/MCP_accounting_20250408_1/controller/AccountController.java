package com.example.MCP_accounting_20250408_1.controller;

import com.example.MCP_accounting_20250408_1.model.Account;
import com.example.MCP_accounting_20250408_1.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    
    @GetMapping
    public String getAllAccounts(Model model) {
        List<Account> accounts = accountService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        model.addAttribute("account", new Account());  // 신규 계정 생성용
        model.addAttribute("title", "계정 관리");
        return "accounts/list";
    }
    
    @PostMapping
    public String createAccount(@ModelAttribute Account account) {
        accountService.createAccount(account);
        return "redirect:/accounts";
    }
    
    @GetMapping("/{id}")
    public String getAccountDetails(@PathVariable Long id, Model model) {
        Account account = accountService.getAccountById(id);
        BigDecimal balance = accountService.getAccountBalance(id);
        
        model.addAttribute("account", account);
        model.addAttribute("balance", balance);
        model.addAttribute("title", account.getAccountName() + " 계정 상세");
        
        return "accounts/detail";
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Account account = accountService.getAccountById(id);
        model.addAttribute("account", account);
        model.addAttribute("title", "계정 정보 수정");
        return "accounts/edit";
    }
    
    @PostMapping("/{id}")
    public String updateAccount(@PathVariable Long id, @ModelAttribute Account account) {
        accountService.updateAccount(id, account);
        return "redirect:/accounts";
    }
    
    @GetMapping("/{id}/delete")
    public String deleteAccount(@PathVariable Long id) {
        boolean deleted = accountService.deleteAccount(id);
        return "redirect:/accounts";
    }
    
    @GetMapping("/types/{type}")
    public String getAccountsByType(@PathVariable String type, Model model) {
        List<Account> accounts = accountService.getAccountsByType(type);
        model.addAttribute("accounts", accounts);
        model.addAttribute("accountType", type);
        model.addAttribute("title", type + " 계정 목록");
        return "accounts/by-type";
    }
    
    @GetMapping("/balance-sheet")
    public String getBalanceSheet(Model model) {
        Map<String, Object> balanceSheetData = accountService.getBalanceSheetData();
        model.addAttribute("balanceSheet", balanceSheetData);
        model.addAttribute("title", "대차대조표");
        return "financial-statements/balance-sheet";
    }
    
    @GetMapping("/income-statement")
    public String getIncomeStatement(Model model) {
        Map<String, Object> incomeStatementData = accountService.getIncomeStatementData();
        model.addAttribute("incomeStatement", incomeStatementData);
        model.addAttribute("title", "손익계산서");
        return "financial-statements/income-statement";
    }
}