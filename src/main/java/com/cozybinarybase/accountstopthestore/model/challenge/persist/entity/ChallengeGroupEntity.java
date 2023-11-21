package com.cozybinarybase.accountstopthestore.model.challenge.persist.entity;

import com.cozybinarybase.accountstopthestore.BaseTimeEntity;
import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

@Entity(name = "challenge_group")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeGroupEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "challengeGroupId", nullable = false, updatable = false)
  private Long id;

  private String name;

  private String description;

  private Long targetAmount;

  private Long maxMembers;

  private LocalDate startAt;

  private LocalDate endAt;

  @ManyToOne(fetch = FetchType.LAZY)
  private MemberEntity admin;

  private String inviteLink;

  private LocalDateTime linkExpiredAt;

}
