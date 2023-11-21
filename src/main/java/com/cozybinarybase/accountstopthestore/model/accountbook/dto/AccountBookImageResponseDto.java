package com.cozybinarybase.accountstopthestore.model.accountbook.dto;

import com.cozybinarybase.accountstopthestore.model.images.persist.entity.ImageEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AccountBookImageResponseDto {

  private Long imageId;
  private String imageUrl;
  private String thumbnailUrl;

  public static AccountBookImageResponseDto of(ImageEntity imageEntity) {
    return AccountBookImageResponseDto.builder()
        .imageId(imageEntity.getId())
        .imageUrl(imageEntity.getImageFileName())
        .thumbnailUrl(imageEntity.getImageFileName())
        .build();
  }
}
