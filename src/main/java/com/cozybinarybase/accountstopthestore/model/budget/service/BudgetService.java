package com.cozybinarybase.accountstopthestore.model.budget.service;

import com.cozybinarybase.accountstopthestore.model.asset.dto.AssetSaveResponseDto;
import com.cozybinarybase.accountstopthestore.model.asset.persist.entity.AssetEntity;
import com.cozybinarybase.accountstopthestore.model.budget.dto.BudgetResponseDto;
import com.cozybinarybase.accountstopthestore.model.budget.dto.BudgetSaveRequestDto;
import com.cozybinarybase.accountstopthestore.model.budget.persist.entity.BudgetEntity;
import com.cozybinarybase.accountstopthestore.model.budget.persist.repository.BudgetRepository;
import com.cozybinarybase.accountstopthestore.model.member.domain.Member;
import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
import com.cozybinarybase.accountstopthestore.model.member.service.MemberService;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetService {
  private final BudgetRepository budgetRepository;
  private final MemberService memberService;

  @Transactional
  public BudgetResponseDto saveBudget(BudgetSaveRequestDto requestDto, Member member) {
    MemberEntity memberEntity = memberService.validateAndGetMember(member);

    LocalDate yearMonth = requestDto.getYearMonth().atEndOfMonth();
    BudgetEntity budgetEntity = budgetRepository.findByYearMonthAndMember(yearMonth, memberEntity)
        .orElse(BudgetEntity.builder()
            .yearMonth(yearMonth)
            .member(memberEntity)
            .build());

    budgetEntity.setBudget(requestDto.getBudget());
    budgetRepository.save(budgetEntity);

    return BudgetResponseDto.fromEntity(budgetEntity);
  }

  public BudgetResponseDto getBudgetByYearMonth(YearMonth yearMonth, Member member) {
    MemberEntity memberEntity = memberService.validateAndGetMember(member);
    LocalDate endOfMonth = yearMonth.atEndOfMonth();
    BudgetEntity budgetEntity = budgetRepository.findByYearMonthAndMember(endOfMonth, memberEntity)
        .orElseThrow(() -> new RuntimeException("설정된 예산이 존재하지 않습니다. 먼저 예산을 설정해주세요"));

    return BudgetResponseDto.fromEntity(budgetEntity);
  }
}
