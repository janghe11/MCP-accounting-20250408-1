package com.example.MCP_accounting_20250408_1.repository;

import com.example.MCP_accounting_20250408_1.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByAccountType(String accountType);
    Account findByAccountName(String accountName);
}
