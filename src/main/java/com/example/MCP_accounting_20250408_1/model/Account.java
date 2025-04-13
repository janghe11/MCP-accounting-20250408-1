package com.example.MCP_accounting_20250408_1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String accountName;
    private String accountType;  // 자산, 부채, 자본, 수익, 비용
    private String description;
    
    // 생성자 오버로딩
    public Account(String accountName, String accountType, String description) {
        this.accountName = accountName;
        this.accountType = accountType;
        this.description = description;
    }
}
