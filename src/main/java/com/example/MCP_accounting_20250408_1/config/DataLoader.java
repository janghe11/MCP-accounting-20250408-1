package com.example.MCP_accounting_20250408_1.config;

import com.example.MCP_accounting_20250408_1.model.Account;
import com.example.MCP_accounting_20250408_1.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final AccountRepository accountRepository;

    @Autowired
    public DataLoader(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 기본 계정 생성
        loadAccounts();
    }

    private void loadAccounts() {
        // 자산 계정
        accountRepository.save(new Account("현금", "자산", "현금 및 현금성 자산"));
        accountRepository.save(new Account("은행예금", "자산", "은행 계좌의 예금"));
        accountRepository.save(new Account("매출채권", "자산", "고객으로부터 받을 금액"));
        accountRepository.save(new Account("재고자산", "자산", "판매를 위한 상품 및 자재"));
        
        // 부채 계정
        accountRepository.save(new Account("매입채무", "부채", "공급업체에 지불해야 할 금액"));
        accountRepository.save(new Account("단기차입금", "부채", "1년 이내에 상환해야 하는 차입금"));
        accountRepository.save(new Account("장기차입금", "부채", "1년 이상의 기간에 상환해야 하는 차입금"));
        
        // 자본 계정
        accountRepository.save(new Account("자본금", "자본", "주주가 투자한 금액"));
        accountRepository.save(new Account("이익잉여금", "자본", "누적된 순이익"));
        
        // 수익 계정
        accountRepository.save(new Account("상품매출", "수익", "상품 판매로 인한 수익"));
        accountRepository.save(new Account("서비스매출", "수익", "서비스 제공으로 인한 수익"));
        accountRepository.save(new Account("이자수익", "수익", "예금 및 대여금에서 발생한 이자"));
        
        // 비용 계정
        accountRepository.save(new Account("급여", "비용", "직원 급여 및 임금"));
        accountRepository.save(new Account("임차료", "비용", "건물, 장비 등의 임차 비용"));
        accountRepository.save(new Account("수도광열비", "비용", "전기, 수도, 가스 등의 유틸리티 비용"));
        accountRepository.save(new Account("광고선전비", "비용", "마케팅 및 광고 비용"));
    }
}
