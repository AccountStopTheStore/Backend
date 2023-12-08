package com.cozybinarybase.accountstopthestore.model.challenge.dto;

import com.cozybinarybase.accountstopthestore.model.challenge.persist.entity.ChallengeGroupEntity;
import com.cozybinarybase.accountstopthestore.model.challenge.persist.entity.MemberGroupEntity;
import com.cozybinarybase.accountstopthestore.model.member.domain.Member;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
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

  private int currentMembers;

  private LocalDate startAt;

  private LocalDate endAt;

  private String inviteLink;

  private Long adminId;

  private Long viewerId;

  private String viewerName;

  private String viewerEmail;

  private List<GroupMemberDto> groupMembers;

  public static ChallengeGroupResponseDto fromEntity(ChallengeGroupEntity challengeGroupEntity) {
    return ChallengeGroupResponseDto.builder()
        .id(challengeGroupEntity.getId())
        .name(challengeGroupEntity.getName())
        .description(challengeGroupEntity.getDescription())
        .targetAmount(challengeGroupEntity.getTargetAmount())
        .maxMembers(challengeGroupEntity.getMaxMembers())
        .startAt(challengeGroupEntity.getStartAt())
        .endAt(challengeGroupEntity.getEndAt())
        .inviteLink(challengeGroupEntity.getInviteLink())
        .adminId(challengeGroupEntity.getAdmin().getId())
        .build();
  }

  public static ChallengeGroupResponseDto fromEntity(ChallengeGroupEntity challengeGroupEntity, List<MemberGroupEntity> memberGroupEntities) {
    ChallengeGroupResponseDto dto = ChallengeGroupResponseDto.builder()
        .id(challengeGroupEntity.getId())
        .name(challengeGroupEntity.getName())
        .description(challengeGroupEntity.getDescription())
        .targetAmount(challengeGroupEntity.getTargetAmount())
        .maxMembers(challengeGroupEntity.getMaxMembers())
        .startAt(challengeGroupEntity.getStartAt())
        .endAt(challengeGroupEntity.getEndAt())
        .inviteLink(challengeGroupEntity.getInviteLink())
        .adminId(challengeGroupEntity.getAdmin().getId())
        .currentMembers(memberGroupEntities.size())
        .groupMembers(memberGroupEntities.stream()
            .map(GroupMemberDto::fromEntity)
            .collect(Collectors.toList()))
        .build();

    return dto;
  }

  public static List<ChallengeGroupResponseDto> fromEntities(List<ChallengeGroupEntity> challengeGroupEntities) {
    return challengeGroupEntities.stream()
        .map(ChallengeGroupResponseDto::fromEntity)
        .toList();
  }

  public static List<ChallengeGroupResponseDto> setViewer(List<ChallengeGroupResponseDto> challengeGroupResponseDtos, Member member) {
    return challengeGroupResponseDtos.stream()
        .map(challengeGroupResponseDto -> {
            challengeGroupResponseDto.setViewerId(member.getId());
            challengeGroupResponseDto.setViewerName(member.getName());
            challengeGroupResponseDto.setViewerEmail(member.getEmail());
          return challengeGroupResponseDto;
        })
        .toList();
  }

  @Data
  public static class GroupMemberDto {
    private Long memberId;
    private String memberEmail;
    private String memberName;
    private Long totalSavingAmount;

    public static GroupMemberDto fromEntity(MemberGroupEntity memberGroupEntity) {
      GroupMemberDto dto = new GroupMemberDto();
      dto.setMemberId(memberGroupEntity.getMember().getId());
      dto.setMemberEmail(memberGroupEntity.getMember().getEmail());
      dto.setMemberName(memberGroupEntity.getMember().getName());
      dto.setTotalSavingAmount(memberGroupEntity.getSavedAmount());
      return dto;
    }
  }
}
