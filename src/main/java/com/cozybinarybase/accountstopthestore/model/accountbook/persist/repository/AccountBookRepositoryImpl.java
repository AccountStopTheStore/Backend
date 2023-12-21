package com.cozybinarybase.accountstopthestore.model.accountbook.persist.repository;

import com.cozybinarybase.accountstopthestore.model.accountbook.persist.entity.AccountBookEntity;
import com.cozybinarybase.accountstopthestore.model.accountbook.persist.entity.QAccountBookEntity;
import com.cozybinarybase.accountstopthestore.model.category.persist.entity.QCategoryEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class AccountBookRepositoryImpl implements AccountBookRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public AccountBookRepositoryImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public EntityManager getEntityManager() {
    return entityManager;
  }

  @Override
  public Page<AccountBookEntity> searchByCriteria(String keyword, LocalDate startDate, LocalDate endDate,
      String categoryName, Long minPrice, Long maxPrice,
      Long memberId, Pageable pageable) {
    QAccountBookEntity accountBook = QAccountBookEntity.accountBookEntity;
    QCategoryEntity category = QCategoryEntity.categoryEntity;

    BooleanExpression keywordCondition = null;

    if (keyword != null) {
      keywordCondition = accountBook.address.contains(keyword)
          .or(accountBook.memo.contains(keyword))
          .or(accountBook.transactionDetail.contains(keyword));
    }

    BooleanExpression dateCondition = null;

    if (startDate != null && endDate != null) {
      dateCondition = accountBook.transactedAt.between(startDate.atStartOfDay(), endDate.atStartOfDay());
    } else if (startDate != null) {
      dateCondition = accountBook.transactedAt.goe(startDate.atStartOfDay());
    } else if (endDate != null) {
      dateCondition = accountBook.transactedAt.loe(endDate.atStartOfDay());
    }

    BooleanExpression memberCondition = accountBook.member.id.eq(memberId);

    BooleanExpression categoryCondition = null;

    if (categoryName != null) {
      categoryCondition = category.name.eq(categoryName);
    }

    BooleanExpression priceCondition = null;
    if (minPrice != null && maxPrice != null) {
      priceCondition = accountBook.amount.between(minPrice, maxPrice);
    } else if (minPrice != null) {
      priceCondition = accountBook.amount.goe(minPrice);
    } else if (maxPrice != null) {
      priceCondition = accountBook.amount.loe(maxPrice);
    }

    JPAQuery<AccountBookEntity> query = queryFactory
        .selectFrom(accountBook)
        .leftJoin(accountBook.category, category)
        .where(keywordCondition, dateCondition, categoryCondition, priceCondition, memberCondition);

    if (pageable.isUnpaged()) {
      // 페이지 정보가 없는 경우 전체 결과를 반환
      List<AccountBookEntity> results = query.fetch();
      return new PageImpl<>(results);
    } else {
      // 페이지 정보가 있는 경우 페이징 적용
      query.offset(pageable.getOffset())
          .limit(pageable.getPageSize());
      List<AccountBookEntity> results = query.fetch();
      long total = query.fetchCount();
      return new PageImpl<>(results, pageable, total);
    }
  }
}
