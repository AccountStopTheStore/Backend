package com.cozybinarybase.accountstopthestore.model.accountbook.persist.repository;

import com.cozybinarybase.accountstopthestore.model.accountbook.persist.entity.AccountBookEntity;
import java.time.LocalDate;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBookRepositoryCustom {
  EntityManager getEntityManager();

  Page<AccountBookEntity> searchByCriteria(String keyword, LocalDate startDate, LocalDate endDate,
      String categoryName, Long minPrice, Long maxPrice,
      Long memberId, Pageable pageable);
}