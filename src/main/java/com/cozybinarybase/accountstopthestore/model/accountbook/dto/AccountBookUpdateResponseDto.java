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
public class AccountBookUpdateResponseDto {

  private Long accountId;
  private String categoryName;
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

  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;

  public static AccountBookUpdateResponseDto fromEntity(AccountBookEntity accountBookEntity) {
    List<Long> imageIdList = accountBookEntity.getImages().stream()
        .map(ImageEntity::getId)
        .collect(Collectors.toList());

    return AccountBookUpdateResponseDto.builder()
        .accountId(accountBookEntity.getId())
        .categoryName(accountBookEntity.getCategory().getName())
        .assetName(accountBookEntity.getAsset().getName())
        .amount(accountBookEntity.getAmount())
        .transactionType(accountBookEntity.getTransactionType().getValue())
        .transactionDetail(accountBookEntity.getTransactionDetail())
        .transactedAt(accountBookEntity.getTransactedAt())
        .memo(accountBookEntity.getMemo())
        .imageIds(imageIdList)
        .isInstallment(accountBookEntity.getIsInstallment())
        .createdAt(accountBookEntity.getCreatedAt())
        .build();
  }
}
