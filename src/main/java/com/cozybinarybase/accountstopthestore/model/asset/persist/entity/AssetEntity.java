package com.cozybinarybase.accountstopthestore.model.asset.persist.entity;

import com.cozybinarybase.accountstopthestore.model.asset.dto.constants.AssetGroup;
import com.cozybinarybase.accountstopthestore.model.asset.dto.constants.AssetType;
import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
import java.time.LocalDateTime;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "asset")
public class AssetEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "assetGroup")
  @Enumerated(EnumType.STRING)
  private AssetGroup group;

  @Column(name = "assetType")
  @Enumerated(EnumType.STRING)
  private AssetType type;

  @Column(name = "assetName")
  private String name;

  @Column(name = "amount")
  private Long amount;

  @Column(name = "statementDay")
  private Integer statementDay;

  @Column(name = "dueDay")
  private Integer dueDay;

  @Column(name = "memo", columnDefinition = "TEXT")
  private String memo;

  @CreatedDate
  @Column(name = "createdAt")
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updatedAt")
  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member")
  private MemberEntity member;

}