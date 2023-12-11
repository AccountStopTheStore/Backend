package com.cozybinarybase.accountstopthestore.model.accountbook.dto;

import com.cozybinarybase.accountstopthestore.model.accountbook.persist.entity.AccountBookEntity;
import com.cozybinarybase.accountstopthestore.model.images.persist.entity.ImageEntity;
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
public class AccountBookResponseDto {

  private Long accountId;
  private String categoryName;
  private String assetGroup;
  private String assetType;
  private String assetName;
  private Long amount;
  private String transactionType;
  private String transactionDetail;

  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime transactedAt;

  private String address;
  private String memo;
  private List<Long> imageIds;
  private String recurringType;
  private Boolean isInstallment;
  private Integer installmentMonth;

  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;

  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime updatedAt;

  private Double latitude;
  private Double longitude;

  public static AccountBookResponseDto fromEntity(AccountBookEntity accountBookEntity) {
    List<Long> imageIdList = accountBookEntity.getImages().stream()
        .map(ImageEntity::getImageId)
        .collect(Collectors.toList());

    return AccountBookResponseDto.builder()
        .accountId(accountBookEntity.getId())
        .categoryName(accountBookEntity.getCategory().getName())
        .assetGroup(accountBookEntity.getAsset().getGroup().getValue())
        .assetType(accountBookEntity.getAsset().getType().getValue())
        .assetName(accountBookEntity.getAsset().getName())
        .amount(accountBookEntity.getAmount())
        .transactionType(accountBookEntity.getTransactionType().getValue())
        .transactionDetail(accountBookEntity.getTransactionDetail())
        .transactedAt(accountBookEntity.getTransactedAt())
        .memo(accountBookEntity.getMemo())
        .address(accountBookEntity.getAddress())
        .imageIds(imageIdList)
        .isInstallment(accountBookEntity.getIsInstallment())
        .installmentMonth(accountBookEntity.getInstallmentMonth())
        .createdAt(accountBookEntity.getCreatedAt())
        .updatedAt(accountBookEntity.getUpdatedAt())
        .latitude(accountBookEntity.getLatitude())
        .longitude(accountBookEntity.getLongitude())
        .build();
  }
}
