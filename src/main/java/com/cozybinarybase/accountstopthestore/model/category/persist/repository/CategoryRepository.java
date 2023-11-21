package com.cozybinarybase.accountstopthestore.model.category.persist.repository;

import com.cozybinarybase.accountstopthestore.model.category.dto.constants.CategoryType;
import com.cozybinarybase.accountstopthestore.model.category.persist.entity.CategoryEntity;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

  boolean existsByNameAndTypeAndMember(String name, CategoryType type, Long memberId);

  List<CategoryEntity> findByMember(Long memberId);

  Optional<CategoryEntity> findByNameAndMember(String categoryName, Long memberId);

  void deleteAllByMember(Long id);

  Collection<CategoryEntity> findByMemberAndNameStartingWithIgnoreCase(Long id, String query);
}
