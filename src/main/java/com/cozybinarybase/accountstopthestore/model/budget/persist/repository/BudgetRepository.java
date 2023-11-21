package com.cozybinarybase.accountstopthestore.model.budget.persist.repository;

import com.cozybinarybase.accountstopthestore.model.budget.persist.entity.BudgetEntity;
import java.time.YearMonth;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {
  Optional<BudgetEntity> findByYearMonth(YearMonth yearMonth);
}