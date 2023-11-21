package com.cozybinarybase.accountstopthestore.model.challenge.dto;

import com.cozybinarybase.accountstopthestore.model.challenge.persist.entity.ChallengeGroupEntity;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChallengeGroupResponseDto {

  private Long id;

  private String name;

  private String description;

  private Long targetAmount;

  private Long maxMembers;

  private LocalDate startAt;

  private LocalDate endAt;

  private Long adminId;

  public static ChallengeGroupResponseDto fromEntity(ChallengeGroupEntity challengeGroupEntity) {
    return ChallengeGroupResponseDto.builder()
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

  public static List<ChallengeGroupResponseDto> fromEntities(List<ChallengeGroupEntity> challengeGroupEntities) {
    return challengeGroupEntities.stream()
        .map(ChallengeGroupResponseDto::fromEntity)
        .toList();
  }
}
