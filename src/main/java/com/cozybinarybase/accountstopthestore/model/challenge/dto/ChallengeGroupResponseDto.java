package com.cozybinarybase.accountstopthestore.model.challenge.dto;

import com.cozybinarybase.accountstopthestore.model.challenge.persist.entity.ChallengeGroupEntity;
import com.cozybinarybase.accountstopthestore.model.member.domain.Member;
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

  private Long viewerId;

  private String viewerName;

  private String viewerEmail;

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

  public static List<ChallengeGroupResponseDto> setViewer(List<ChallengeGroupResponseDto> challengeGroupResponseDtos, Member member) {
    return challengeGroupResponseDtos.stream()
        .map(challengeGroupResponseDto -> {
          if (challengeGroupResponseDto.getAdminId().equals(member.getId())) {
            challengeGroupResponseDto.setViewerId(member.getId());
            challengeGroupResponseDto.setViewerName(member.getName());
            challengeGroupResponseDto.setViewerEmail(member.getEmail());
          }
          return challengeGroupResponseDto;
        })
        .toList();
  }
}
