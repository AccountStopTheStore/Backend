package com.cozybinarybase.accountstopthestore.model.asset.dto;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape;

import com.cozybinarybase.accountstopthestore.model.asset.persist.entity.AssetEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AssetUpdateResponseDto {

  private String assetGroup;
  private String assetType;
  private String assetName;
  private Long amount;
  private Integer statementDay;
  private Integer dueDay;
  private String memo;

  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;

  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime updatedAt;

  public static AssetUpdateResponseDto fromEntity(AssetEntity assetEntity) {
    return AssetUpdateResponseDto.builder()
        .assetGroup(assetEntity.getGroup().getValue())
        .assetType(assetEntity.getType().getValue())
        .assetName(assetEntity.getName())
        .amount(assetEntity.getAmount())
        .statementDay(assetEntity.getStatementDay())
        .dueDay(assetEntity.getDueDay())
        .memo(assetEntity.getMemo())
        .createdAt(assetEntity.getCreatedAt())
        .updatedAt(assetEntity.getUpdatedAt())
        .build();
  }
}
