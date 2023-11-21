package com.cozybinarybase.accountstopthestore.model.category.domain;

import com.cozybinarybase.accountstopthestore.model.category.dto.CategorySaveRequestDto;
import com.cozybinarybase.accountstopthestore.model.category.dto.CategoryUpdateRequestDto;
import com.cozybinarybase.accountstopthestore.model.category.dto.constants.CategoryType;
import com.cozybinarybase.accountstopthestore.model.category.persist.entity.CategoryEntity;
import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Component
public class Category {

  private Long id;
  private String name;
  private CategoryType type;
  private Long memberId;

  public static Category fromEntity(CategoryEntity categoryEntity) {
    return Category.builder()
        .id(categoryEntity.getId())
        .name(categoryEntity.getName())
        .type(categoryEntity.getType())
        .memberId(categoryEntity.getMember().getId())
        .build();
  }

  public Category createCategory(CategorySaveRequestDto requestDto, Long memberId) {
    return Category.builder()
        .name(requestDto.getName())
        .type(requestDto.getType())
        .memberId(memberId)
        .build();
  }

  public void updateCategory(CategoryUpdateRequestDto requestDto) {
    if (requestDto.getName() != null) {
      this.name = requestDto.getName();
    }
    if (requestDto.getType() != null) {
      this.type = requestDto.getType();
    }
  }

  public CategoryEntity toEntity() {
    return CategoryEntity.builder()
        .id(this.id)
        .name(this.name)
        .type(this.type)
        .member(MemberEntity.builder().id(this.memberId).build())
        .build();
  }
}
