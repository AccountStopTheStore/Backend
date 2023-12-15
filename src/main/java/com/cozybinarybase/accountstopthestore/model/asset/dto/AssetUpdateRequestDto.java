package com.cozybinarybase.accountstopthestore.model.asset.dto;

import com.cozybinarybase.accountstopthestore.model.asset.dto.constants.AssetGroup;
import com.cozybinarybase.accountstopthestore.model.asset.dto.constants.AssetType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AssetUpdateRequestDto {

  private AssetGroup assetGroup;
  private AssetType assetType;

  @NotBlank(message = "자산명을 입력해주시길 바랍니다.")
  private String assetName;

  @PositiveOrZero(message = "음수는 입력할 수 없습니다.")
  private Long amount;

  @Positive(message = "양수의 값만 입력할 수 있습니다.")
  private Integer statementDay;

  @Positive(message = "양수의 값만 입력할 수 있습니다.")
  private Integer dueDay;

  private String memo;

  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;

  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime updatedAt;
}
