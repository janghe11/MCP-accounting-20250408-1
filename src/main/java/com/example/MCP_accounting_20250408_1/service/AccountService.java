package com.example.MCP_accounting_20250408_1.service;

import com.example.MCP_accounting_20250408_1.model.Account;
import com.example.MCP_accounting_20250408_1.model.Transaction;
import com.example.MCP_accounting_20250408_1.repository.AccountRepository;
import com.example.MCP_accounting_20250408_1.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    /**
     * 모든 계정 목록 조회
     */
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    
    /**
     * 계정 타입별 계정 목록 조회
     */
    public List<Account> getAccountsByType(String accountType) {
        return accountRepository.findByAccountType(accountType);
    }
    
    /**
     * 계정 ID로 계정 조회
     */
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }
    
    /**
     * 계정 생성
     */
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }
    
    /**
     * 계정 수정
     */
    public Account updateAccount(Long id, Account accountDetails) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent()) {
            Account existingAccount = account.get();
            existingAccount.setAccountName(accountDetails.getAccountName());
            existingAccount.setAccountType(accountDetails.getAccountType());
            existingAccount.setDescription(accountDetails.getDescription());
            return accountRepository.save(existingAccount);
        }
        return null;
    }
    
    /**
     * 계정 삭제 (관련 거래가 없을 경우에만 가능)
     */
    public boolean deleteAccount(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent()) {
            Account accountToDelete = account.get();
            List<Transaction> transactions = transactionRepository.findByDebitAccountOrCreditAccount(
                    accountToDelete, accountToDelete);
            
            if (transactions.isEmpty()) {
                accountRepository.delete(accountToDelete);
                return true;
            }
        }
        return false;
    }
    
    /**
     * 계정의 현재 잔액을 계산
     */
    public BigDecimal getAccountBalance(Long accountId) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (!accountOpt.isPresent()) {
            return BigDecimal.ZERO;
        }
        
        Account account = accountOpt.get();
        List<Transaction> transactions = transactionRepository.findByDebitAccountOrCreditAccount(account, account);
        
        BigDecimal balance = BigDecimal.ZERO;
        
        for (Transaction transaction : transactions) {
            if (transaction.getDebitAccount().equals(account)) {
                // 차변(Debit) 거래: 자산, 비용은 증가, 부채, 자본, 수익은 감소
                if (account.getAccountType().equals("자산") || account.getAccountType().equals("비용")) {
                    balance = balance.add(transaction.getAmount());
                } else {
                    balance = balance.subtract(transaction.getAmount());
                }
            }
            
            if (transaction.getCreditAccount().equals(account)) {
                // 대변(Credit) 거래: 자산, 비용은 감소, 부채, 자본, 수익은 증가
                if (account.getAccountType().equals("자산") || account.getAccountType().equals("비용")) {
                    balance = balance.subtract(transaction.getAmount());
                } else {
                    balance = balance.add(transaction.getAmount());
                }
            }
        }
        
        return balance;
    }
    
    /**
     * 대차대조표 데이터 생성
     */
    public Map<String, Object> getBalanceSheetData() {
        Map<String, Object> balanceSheet = new HashMap<>();
        
        List<Account> assets = accountRepository.findByAccountType("자산");
        List<Account> liabilities = accountRepository.findByAccountType("부채");
        List<Account> equity = accountRepository.findByAccountType("자본");
        
        BigDecimal totalAssets = BigDecimal.ZERO;
        BigDecimal totalLiabilities = BigDecimal.ZERO;
        BigDecimal totalEquity = BigDecimal.ZERO;
        
        Map<String, BigDecimal> assetBalances = new HashMap<>();
        for (Account asset : assets) {
            BigDecimal balance = getAccountBalance(asset.getId());
            assetBalances.put(asset.getAccountName(), balance);
            totalAssets = totalAssets.add(balance);
        }
        
        Map<String, BigDecimal> liabilityBalances = new HashMap<>();
        for (Account liability : liabilities) {
            BigDecimal balance = getAccountBalance(liability.getId());
            liabilityBalances.put(liability.getAccountName(), balance);
            totalLiabilities = totalLiabilities.add(balance);
        }
        
        Map<String, BigDecimal> equityBalances = new HashMap<>();
        for (Account equityAccount : equity) {
            BigDecimal balance = getAccountBalance(equityAccount.getId());
            equityBalances.put(equityAccount.getAccountName(), balance);
            totalEquity = totalEquity.add(balance);
        }
        
        balanceSheet.put("assetAccounts", assetBalances);
        balanceSheet.put("liabilityAccounts", liabilityBalances);
        balanceSheet.put("equityAccounts", equityBalances);
        balanceSheet.put("totalAssets", totalAssets);
        balanceSheet.put("totalLiabilities", totalLiabilities);
        balanceSheet.put("totalEquity", totalEquity);
        
        return balanceSheet;
    }
    
    /**
     * 손익계산서 데이터 생성
     */
    public Map<String, Object> getIncomeStatementData() {
        Map<String, Object> incomeStatement = new HashMap<>();
        
        List<Account> revenues = accountRepository.findByAccountType("수익");
        List<Account> expenses = accountRepository.findByAccountType("비용");
        
        BigDecimal totalRevenues = BigDecimal.ZERO;
        BigDecimal totalExpenses = BigDecimal.ZERO;
        
        Map<String, BigDecimal> revenueBalances = new HashMap<>();
        for (Account revenue : revenues) {
            BigDecimal balance = getAccountBalance(revenue.getId());
            revenueBalances.put(revenue.getAccountName(), balance);
            totalRevenues = totalRevenues.add(balance);
        }
        
        Map<String, BigDecimal> expenseBalances = new HashMap<>();
        for (Account expense : expenses) {
            BigDecimal balance = getAccountBalance(expense.getId());
            expenseBalances.put(expense.getAccountName(), balance);
            totalExpenses = totalExpenses.add(balance);
        }
        
        BigDecimal netIncome = totalRevenues.subtract(totalExpenses);
        
        incomeStatement.put("revenueAccounts", revenueBalances);
        incomeStatement.put("expenseAccounts", expenseBalances);
        incomeStatement.put("totalRevenues", totalRevenues);
        incomeStatement.put("totalExpenses", totalExpenses);
        incomeStatement.put("netIncome", netIncome);
        
        return incomeStatement;
    }
    
    /**
     * 기본 계정 생성 (초기 데이터용)
     */
    public void createDefaultAccounts() {
        if (accountRepository.count() == 0) {
            // 자산 계정
            accountRepository.save(new Account("현금", "자산", "현금 및 현금성 자산"));
            accountRepository.save(new Account("보통예금", "자산", "은행 보통예금 계좌"));
            accountRepository.save(new Account("매출채권", "자산", "상품 및 서비스 판매 후 받을 금액"));
            accountRepository.save(new Account("재고자산", "자산", "판매를 위해 보유한 상품"));
            accountRepository.save(new Account("비품", "자산", "사무용 비품"));
            
            // 부채 계정
            accountRepository.save(new Account("매입채무", "부채", "상품 및 서비스 구매 후 지급할 금액"));
            accountRepository.save(new Account("단기차입금", "부채", "1년 이내 상환 예정인 차입금"));
            accountRepository.save(new Account("미지급비용", "부채", "발생한 비용 중 지급되지 않은 금액"));
            
            // 자본 계정
            accountRepository.save(new Account("자본금", "자본", "주주가 출자한 금액"));
            accountRepository.save(new Account("이익잉여금", "자본", "누적된 미처분 이익"));
            
            // 수익 계정
            accountRepository.save(new Account("매출", "수익", "상품 판매로 인한 수익"));
            accountRepository.save(new Account("이자수익", "수익", "금융상품에서 발생한 이자 수익"));
            
            // 비용 계정
            accountRepository.save(new Account("매출원가", "비용", "판매된 상품의 원가"));
            accountRepository.save(new Account("급여", "비용", "직원에게 지급한 급여"));
            accountRepository.save(new Account("임차료", "비용", "건물 및 장비 임차 비용"));
            accountRepository.save(new Account("수도광열비", "비용", "수도, 전기, 가스 등의 비용"));
        }
    }
}