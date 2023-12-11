package com.cozybinarybase.accountstopthestore.model.asset.dto;

import com.cozybinarybase.accountstopthestore.model.asset.persist.entity.AssetEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AssetSaveResponseDto {

  private Long assetId;
  private String assetGroup;
  private String assetType;
  private String assetName;
  private Integer statementDay;
  private Integer dueDay;
  private Long amount;
  private String memo;

  public static AssetSaveResponseDto fromEntity(AssetEntity assetEntity) {
    return AssetSaveResponseDto.builder()
        .assetId(assetEntity.getId())
        .assetGroup(assetEntity.getGroup().getValue())
        .assetType(assetEntity.getType().getValue())
        .assetName(assetEntity.getName())
        .statementDay(assetEntity.getStatementDay())
        .dueDay(assetEntity.getDueDay())
        .amount(assetEntity.getAmount())
        .memo(assetEntity.getMemo())
        .build();
  }
}
