package com.example.MCP_accounting_20250408_1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate transactionDate;
    private String description;
    
    @ManyToOne
    private Account debitAccount;
    
    @ManyToOne
    private Account creditAccount;
    
    private BigDecimal amount;
    private String reference;
}
