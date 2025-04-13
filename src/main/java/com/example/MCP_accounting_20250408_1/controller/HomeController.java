package com.example.MCP_accounting_20250408_1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "회계 학습 시스템");
        model.addAttribute("message", "회계 학습을 위한 웹 애플리케이션에 오신 것을 환영합니다!");
        return "home";
    }
    
    @GetMapping("/basics")
    public String accountingBasics(Model model) {
        model.addAttribute("title", "회계 기초");
        model.addAttribute("content", "회계의 기본 원칙과 개념에 대해 알아봅니다.");
        return "basics";
    }
    
    @GetMapping("/ledger")
    public String ledger(Model model) {
        model.addAttribute("title", "원장 관리");
        model.addAttribute("content", "원장 기입과 관리 방법을 배웁니다.");
        return "ledger";
    }
    
    @GetMapping("/financial-statements")
    public String financialStatements(Model model) {
        model.addAttribute("title", "재무제표");
        model.addAttribute("content", "대차대조표, 손익계산서, 현금흐름표의 이해와 분석");
        return "financial-statements";
    }
}
