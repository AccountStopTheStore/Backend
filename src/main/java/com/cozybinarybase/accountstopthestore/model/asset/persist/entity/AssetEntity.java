package com.cozybinarybase.accountstopthestore.model.asset.persist.entity;

import com.cozybinarybase.accountstopthestore.BaseTimeEntity;
import com.cozybinarybase.accountstopthestore.model.asset.dto.constants.AssetType;
import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "asset")
public class AssetEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "assetId", nullable = false, updatable = false)
  private Long id;

  @Column(name = "assetType")
  @Enumerated(EnumType.STRING)
  private AssetType type;

  @Column(name = "name")
  private String name;

  @Column(name = "amount")
  private Long amount;

  @Column(name = "statementDay")
  private Integer statementDay;

  @Column(name = "dueDay")
  private Integer dueDay;

  @Column(name = "memo", columnDefinition = "TEXT")
  private String memo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member")
  private MemberEntity member;
}