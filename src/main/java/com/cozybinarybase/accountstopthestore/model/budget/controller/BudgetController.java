package com.cozybinarybase.accountstopthestore.model.budget.controller;

import com.cozybinarybase.accountstopthestore.model.budget.dto.BudgetResponseDto;
import com.cozybinarybase.accountstopthestore.model.budget.dto.BudgetSaveRequestDto;
import com.cozybinarybase.accountstopthestore.model.budget.persist.entity.BudgetEntity;
import com.cozybinarybase.accountstopthestore.model.budget.service.BudgetService;
import com.cozybinarybase.accountstopthestore.model.member.domain.Member;
import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
import com.cozybinarybase.accountstopthestore.model.member.service.MemberService;
import java.time.LocalDate;
import java.time.YearMonth;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetController {

  private final BudgetService budgetService;

  @PostMapping
  public ResponseEntity<BudgetResponseDto> createBudget(
      @RequestBody @Valid BudgetSaveRequestDto budget,
      @AuthenticationPrincipal Member member) {
    BudgetResponseDto responseDto = budgetService.saveBudget(budget, member);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  @GetMapping
  public ResponseEntity<BudgetResponseDto> getBudget(
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth,
      @AuthenticationPrincipal Member member) {

    BudgetResponseDto responseDto = budgetService.getBudgetByYearMonth(yearMonth, member);
    return ResponseEntity.ok(responseDto);
  }
}
