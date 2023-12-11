package com.cozybinarybase.accountstopthestore.model.asset.domain;

import com.cozybinarybase.accountstopthestore.model.asset.dto.AssetSaveRequestDto;
import com.cozybinarybase.accountstopthestore.model.asset.dto.AssetUpdateRequestDto;
import com.cozybinarybase.accountstopthestore.model.asset.dto.constants.AssetGroup;
import com.cozybinarybase.accountstopthestore.model.asset.dto.constants.AssetType;
import com.cozybinarybase.accountstopthestore.model.asset.persist.entity.AssetEntity;
import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
import java.time.LocalDateTime;
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
public class Asset {

  private Long id;
  private AssetGroup group;
  private AssetType type;
  private String name;
  private Long amount;
  private Integer statementDay;
  private Integer dueDay;
  private String memo;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Long memberId;

  public Asset createAsset(AssetSaveRequestDto requestDto, Long memberId) {

    return Asset.builder()
        .group(requestDto.getAssetGroup())
        .type(requestDto.getAssetType())
        .name(requestDto.getAssetName())
        .amount(requestDto.getAmount())
        .statementDay(requestDto.getStatementDay())
        .dueDay(requestDto.getDueDay())
        .memo(requestDto.getMemo())
        .memberId(memberId)
        .build();
  }

  public void update(AssetUpdateRequestDto requestDto) {
    if (requestDto.getAssetGroup() != null) {
      this.group = requestDto.getAssetGroup();
    }
    if (requestDto.getAssetType() != null) {
      this.type = requestDto.getAssetType();
    }
    if (requestDto.getAssetName() != null) {
      this.name = requestDto.getAssetName();
    }
    if (requestDto.getAmount() != null) {
      this.amount = requestDto.getAmount();
    }
    if (requestDto.getStatementDay() != null) {
      this.statementDay = requestDto.getStatementDay();
    }
    if (requestDto.getDueDay() != null) {
      this.dueDay = requestDto.getDueDay();
    }
    if (requestDto.getMemo() != null) {
      this.memo = requestDto.getMemo();
    }
    if (requestDto.getCreatedAt() != null) {
      this.createdAt = requestDto.getCreatedAt();
    }
    if (requestDto.getUpdatedAt() != null) {
      this.updatedAt = requestDto.getUpdatedAt();
    }
  }

  public AssetEntity toEntity() {
    return AssetEntity.builder()
        .id(this.id)
        .group(this.group)
        .type(this.type)
        .name(this.name)
        .amount(this.amount)
        .statementDay(this.statementDay)
        .dueDay(this.dueDay)
        .memo(this.memo)
        .createdAt(this.createdAt)
        .updatedAt(this.updatedAt)
        .member(MemberEntity.builder().id(this.memberId).build())
        .build();
  }

  public static Asset fromEntity(AssetEntity assetEntity) {
    return Asset.builder()
        .id(assetEntity.getId())
        .group(assetEntity.getGroup())
        .type(assetEntity.getType())
        .name(assetEntity.getName())
        .amount(assetEntity.getAmount())
        .statementDay(assetEntity.getStatementDay())
        .dueDay(assetEntity.getDueDay())
        .memo(assetEntity.getMemo())
        .createdAt(assetEntity.getCreatedAt())
        .updatedAt(assetEntity.getUpdatedAt())
        .memberId(assetEntity.getMember().getId())
        .build();
  }
}
