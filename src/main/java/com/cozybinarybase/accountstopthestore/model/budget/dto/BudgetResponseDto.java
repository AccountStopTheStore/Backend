package com.cozybinarybase.accountstopthestore.model.budget.dto;

import com.cozybinarybase.accountstopthestore.model.budget.persist.entity.BudgetEntity;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BudgetResponseDto {
  private String yearMonth;
  private Long budget;

  public static BudgetResponseDto fromEntity(BudgetEntity entity) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
    String formattedYearMonth = entity.getYearMonth().format(formatter);

    return BudgetResponseDto.builder()
        .yearMonth(formattedYearMonth)
        .budget(entity.getBudget())
        .build();
  }
}