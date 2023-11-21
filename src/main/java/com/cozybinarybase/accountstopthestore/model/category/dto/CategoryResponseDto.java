package com.cozybinarybase.accountstopthestore.model.category.dto;

import com.cozybinarybase.accountstopthestore.model.category.persist.entity.CategoryEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CategoryResponseDto {

  private Long id;
  private String type;
  private String name;

  public static CategoryResponseDto fromEntity(CategoryEntity categoryEntity) {
    return CategoryResponseDto.builder()
        .id(categoryEntity.getId())
        .type(categoryEntity.getType().getValue())
        .name(categoryEntity.getName())
        .build();
  }

  public static List<CategoryResponseDto> fromEntities(List<CategoryEntity> categoryEntities) {
    return categoryEntities.stream()
        .map(CategoryResponseDto::fromEntity)
        .collect(Collectors.toList());
  }
}
