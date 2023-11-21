package com.cozybinarybase.accountstopthestore.model.accountbook.persist.entity;

import com.cozybinarybase.accountstopthestore.BaseTimeEntity;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.constants.TransactionType;
import com.cozybinarybase.accountstopthestore.model.asset.persist.entity.AssetEntity;
import com.cozybinarybase.accountstopthestore.model.category.persist.entity.CategoryEntity;
import com.cozybinarybase.accountstopthestore.model.images.persist.entity.ImageEntity;
import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
import com.querydsl.core.annotations.QueryEntity;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@QueryEntity
@Entity(name = "AccountBook")
public class AccountBookEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "accountId", nullable = false, updatable = false)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "transactionType", nullable = false)
  private TransactionType transactionType;

  @Column(name = "transactionDetail", nullable = false)
  private String transactionDetail;

  @Column(name = "transactedAt", nullable = false)
  private LocalDateTime transactedAt;

  @Column(name = "amount", nullable = false)
  private Long amount;

  @Column(name = "address", columnDefinition = "TEXT")
  private String address;

  @Column(name = "memo", columnDefinition = "TEXT")
  private String memo;

  @Column(name = "isInstallment", nullable = false)
  private Boolean isInstallment;

  @Column(name = "latitude")
  private Double latitude;

  @Column(name = "longitude")
  private Double longitude;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category")
  private CategoryEntity category;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member")
  private MemberEntity member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "asset")
  private AssetEntity asset;

  @OneToMany(mappedBy = "accountBook", cascade = CascadeType.ALL)
  private List<ImageEntity> images;
}
