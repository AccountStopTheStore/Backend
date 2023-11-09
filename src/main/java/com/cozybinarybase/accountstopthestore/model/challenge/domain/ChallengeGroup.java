package com.cozybinarybase.accountstopthestore.model.challenge.domain;

import com.cozybinarybase.accountstopthestore.model.challenge.dto.ChallengeGroupRequestDto;
import com.cozybinarybase.accountstopthestore.model.challenge.persist.entity.ChallengeGroupEntity;
import com.cozybinarybase.accountstopthestore.model.member.domain.Member;
import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeGroup {

  private Long id;

  private String name;

  private String description;

  private Long targetAmount;

  private Long maxMembers;

  private LocalDateTime startAt;

  private LocalDateTime endAt;

  private Long adminId;

  public static ChallengeGroup fromRequest(ChallengeGroupRequestDto groupRequest, Member member) {
    return ChallengeGroup.builder()
        .name(groupRequest.getName())
        .description(groupRequest.getDescription())
        .targetAmount(groupRequest.getTargetAmount())
        .maxMembers(groupRequest.getMaxMembers())
        .startAt(groupRequest.getStartAt())
        .endAt(groupRequest.getEndAt())
        .adminId(member.getId())
        .build();
  }

  public static ChallengeGroup fromEntity(ChallengeGroupEntity challengeGroupEntity) {
    return ChallengeGroup.builder()
        .id(challengeGroupEntity.getId())
        .name(challengeGroupEntity.getName())
        .description(challengeGroupEntity.getDescription())
        .targetAmount(challengeGroupEntity.getTargetAmount())
        .maxMembers(challengeGroupEntity.getMaxMembers())
        .startAt(challengeGroupEntity.getStartAt())
        .endAt(challengeGroupEntity.getEndAt())
        .adminId(challengeGroupEntity.getAdmin().getId())
        .build();
  }

  public ChallengeGroupEntity toEntity() {
    return ChallengeGroupEntity.builder()
        .id(id)
        .name(name)
        .description(description)
        .targetAmount(targetAmount)
        .maxMembers(maxMembers)
        .startAt(startAt)
        .endAt(endAt)
        .admin(MemberEntity.builder().id(adminId).build())
        .build();
  }
}
