package com.cozybinarybase.accountstopthestore.model.asset.dto;

import com.cozybinarybase.accountstopthestore.model.asset.persist.entity.AssetEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
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
public class AssetResponseDto {

  private Long assetId;
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

  public static AssetResponseDto fromEntity(AssetEntity assetEntity) {
    return AssetResponseDto.builder()
        .assetId(assetEntity.getId())
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

  public static List<AssetResponseDto> fromEntities(List<AssetEntity> assetEntities) {
    return assetEntities.stream()
        .map(AssetResponseDto::fromEntity)
        .collect(Collectors.toList());
  }
}
