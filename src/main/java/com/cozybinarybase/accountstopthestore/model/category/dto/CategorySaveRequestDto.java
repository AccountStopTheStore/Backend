package com.cozybinarybase.accountstopthestore.model.category.dto;

import com.cozybinarybase.accountstopthestore.model.category.dto.constants.CategoryType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategorySaveRequestDto {

  @NotBlank(message = "카테고리명을 입력해주세요.")
  private String name;

  @NotNull(message = "카테고리 유형을 입력해주세요.")
  private CategoryType type;
}
