package com.cozybinarybase.accountstopthestore.model.accountbook.domain;

import com.cozybinarybase.accountstopthestore.model.accountbook.dto.AccountBookSaveRequestDto;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.AccountBookUpdateRequestDto;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.constants.TransactionType;
import com.cozybinarybase.accountstopthestore.model.accountbook.persist.entity.AccountBookEntity;
import com.cozybinarybase.accountstopthestore.model.asset.persist.entity.AssetEntity;
import com.cozybinarybase.accountstopthestore.model.category.persist.entity.CategoryEntity;
import com.cozybinarybase.accountstopthestore.model.images.persist.entity.ImageEntity;
import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
import java.time.LocalDateTime;
import java.util.List;
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
public class AccountBook {

  private Long id;
  private TransactionType transactionType;
  private String transactionDetail;
  private LocalDateTime transactedAt;
  private Long amount;
  private String address;
  private String memo;
  private Boolean isInstallment;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Long categoryId;
  private Long memberId;
  private Long assetId;
  private String categoryName;
  private String assetName;
  private List<ImageEntity> images;

  public static AccountBook fromEntity(AccountBookEntity accountBookEntity) {
    return AccountBook.builder()
        .id(accountBookEntity.getId())
        .transactionType(accountBookEntity.getTransactionType())
        .transactionDetail(accountBookEntity.getTransactionDetail())
        .transactedAt(accountBookEntity.getTransactedAt())
        .amount(accountBookEntity.getAmount())
        .address(accountBookEntity.getAddress())
        .memo(accountBookEntity.getMemo())
        .isInstallment(accountBookEntity.getIsInstallment())
        .createdAt(accountBookEntity.getCreatedAt())
        .updatedAt(accountBookEntity.getUpdatedAt())
        .categoryId(accountBookEntity.getCategory().getId())
        .memberId(accountBookEntity.getMember().getId())
        .assetId(accountBookEntity.getAsset().getId())
        .categoryName(accountBookEntity.getCategory().getName())
        .assetName(accountBookEntity.getAsset().getName())
        .images(accountBookEntity.getImages())
        .build();
  }

  public AccountBook createAccountBook(AccountBookSaveRequestDto requestDto, Long categoryId,
      Long assetId, Long memberId, String categoryName, String assetName, List<ImageEntity> images) {
    return AccountBook.builder()
        .transactionType(requestDto.getTransactionType())
        .transactionDetail(requestDto.getTransactionDetail())
        .transactedAt(requestDto.getTransactedAt())
        .amount(requestDto.getAmount())
        .memo(requestDto.getMemo())
        .address(requestDto.getAddress())
        .isInstallment(requestDto.getIsInstallment())
        .categoryId(categoryId)
        .assetId(assetId)
        .memberId(memberId)
        .categoryName(categoryName)
        .assetName(assetName)
        .images(images)
        .build();
  }

  public void updateAccountBook(AccountBookUpdateRequestDto requestDto,
      Long categoryId, Long assetId, List<ImageEntity> images) {
    if (requestDto.getCategoryName() != null) {
      this.categoryName = requestDto.getCategoryName();
      this.categoryId = categoryId;
    }
    if (requestDto.getAssetName() != null) {
      this.assetName = requestDto.getAssetName();
      this.assetId = assetId;
    }
    if (requestDto.getAmount() != null) {
      this.amount = requestDto.getAmount();
    }
    if (requestDto.getTransactionType() != null) {
      this.transactionType = requestDto.getTransactionType();
    }
    if (requestDto.getTransactionDetail() != null) {
      this.transactionDetail = requestDto.getTransactionDetail();
    }
    if (requestDto.getTransactedAt() != null) {
      this.transactedAt = requestDto.getTransactedAt();
    }
    if (requestDto.getMemo() != null) {
      this.memo = requestDto.getMemo();
    }
    if (requestDto.getAddress() != null) {
      this.address = requestDto.getAddress();
    }
    if (requestDto.getIsInstallment() != null) {
      this.isInstallment = requestDto.getIsInstallment();
    }
    if (images != null) {
      this.images = images;
    }
  }

  public AccountBookEntity toEntity() {
    return AccountBookEntity.builder()
        .id(this.id)
        .transactionType(this.transactionType)
        .transactionDetail(this.transactionDetail)
        .transactedAt(this.transactedAt)
        .amount(this.amount)
        .address(this.address)
        .memo(this.memo)
        .isInstallment(this.isInstallment)
        .category(CategoryEntity.builder().id(this.categoryId).name(this.categoryName).build())
        .member(MemberEntity.builder().id(this.memberId).build())
        .asset(AssetEntity.builder().id(this.assetId).name(this.assetName).build())
        .images(this.images)
        .build();
  }
}
