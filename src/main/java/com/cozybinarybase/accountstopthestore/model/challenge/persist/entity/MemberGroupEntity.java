package com.cozybinarybase.accountstopthestore.model.challenge.persist.entity;

import com.cozybinarybase.accountstopthestore.BaseTimeEntity;
import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "member_group")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberGroupEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "memberGroupId", nullable = false, updatable = false)
  private long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private MemberEntity member;

  @ManyToOne(fetch = FetchType.LAZY)
  private ChallengeGroupEntity challengeGroup;

  private Long savedAmount;
}
