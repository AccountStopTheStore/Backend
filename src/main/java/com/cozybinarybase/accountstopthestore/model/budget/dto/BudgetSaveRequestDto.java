package com.cozybinarybase.accountstopthestore.model.budget.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.YearMonth;
import lombok.Data;

@Data
public class BudgetSaveRequestDto {
  @JsonFormat(pattern = "yyyy-MM")
  private YearMonth yearMonth;

  private Long budget;
}
