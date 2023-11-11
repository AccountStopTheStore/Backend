package com.cozybinarybase.accountstopthestore.model.accountbook.persist.repository;

import com.cozybinarybase.accountstopthestore.model.accountbook.dto.StatisticsData;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.constants.TransactionType;
import com.cozybinarybase.accountstopthestore.model.accountbook.persist.entity.AccountBookEntity;
import com.cozybinarybase.accountstopthestore.model.accountbook.persist.entity.QAccountBookEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBookRepository extends JpaRepository<AccountBookEntity, Long>,
    AccountBookRepositoryCustom {

  Optional<AccountBookEntity> findByIdAndMember_Id(Long accountBookId, Long memberId);

  Page<AccountBookEntity> findByCreatedAtBetweenAndTransactionTypeAndMember_Id(
      LocalDateTime startDate, LocalDateTime endDate, TransactionType transactionType,
      Long memberId, Pageable pageable);

  Page<AccountBookEntity> findByMember_IdAndCategory_NameStartingWithIgnoreCase(Long memberId,
      String keyword, Pageable pageable);

  Page<AccountBookEntity> findByMemoContainingAndTransactedAtBetweenAndCategory_NameAndAmountBetweenAndMember_Id(
      String keyword, LocalDateTime startDate, LocalDateTime endDate, String categoryName,
      Long minPrice, Long maxPrice, Long memberId, Pageable pageable);

  default List<StatisticsData> findTransactionStatistics(LocalDate startDate, LocalDate endDate,
      TransactionType transactionType, Long memberId) {
    QAccountBookEntity accountBook = QAccountBookEntity.accountBookEntity;

    // 쿼리 시작
    JPAQuery<StatisticsData> query = new JPAQuery<>(getEntityManager());
    query.select(
            Projections.constructor(StatisticsData.class, accountBook.category.name.as("category"),
                accountBook.amount.sum().as("value"))).from(accountBook).where(
            accountBook.member.id.eq(memberId),
            accountBook.transactedAt.between(startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)),
            accountBook.transactionType.eq(transactionType))
        .groupBy(accountBook.category.name, accountBook.asset.id);

    // 결과 반환
    return query.fetch();
  }
}
