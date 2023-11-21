package com.cozybinarybase.accountstopthestore.model.challenge.service;

import com.cozybinarybase.accountstopthestore.model.challenge.domain.ChallengeGroup;
import com.cozybinarybase.accountstopthestore.model.challenge.domain.MemberGroup;
import com.cozybinarybase.accountstopthestore.model.challenge.dto.ChallengeGroupRequestDto;
import com.cozybinarybase.accountstopthestore.model.challenge.dto.ChallengeGroupResponseDto;
import com.cozybinarybase.accountstopthestore.model.challenge.dto.InviteLinkResponseDto;
import com.cozybinarybase.accountstopthestore.model.challenge.dto.MemberGroupResponseDto;
import com.cozybinarybase.accountstopthestore.model.challenge.dto.SavingMoneyRequestDto;
import com.cozybinarybase.accountstopthestore.model.challenge.persist.entity.ChallengeGroupEntity;
import com.cozybinarybase.accountstopthestore.model.challenge.persist.entity.MemberGroupEntity;
import com.cozybinarybase.accountstopthestore.model.challenge.persist.repository.ChallengeGroupRepository;
import com.cozybinarybase.accountstopthestore.model.challenge.persist.repository.MemberGroupRepository;
import com.cozybinarybase.accountstopthestore.model.challenge.security.RandomStringGenerator;
import com.cozybinarybase.accountstopthestore.model.member.domain.Member;
import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
import com.cozybinarybase.accountstopthestore.model.member.persist.repository.MemberRepository;
import com.cozybinarybase.accountstopthestore.model.member.service.MemberService;
import com.cozybinarybase.accountstopthestore.model.message.domain.Message;
import com.cozybinarybase.accountstopthestore.model.message.persist.entity.MessageEntity;
import com.cozybinarybase.accountstopthestore.model.message.persist.repository.MessageRepository;
import com.cozybinarybase.accountstopthestore.model.message.service.MessageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeGroupService {

  private final ChallengeGroupRepository challengeGroupRepository;
  private final MemberGroupRepository memberGroupRepository;
  private final MemberRepository memberRepository;
  private final MemberService memberService;
  private final MessageRepository messageRepository;
  private final MessageService messageService;
  public InviteLinkResponseDto createInviteLink(Long groupId, Member member) {
    ChallengeGroup challengeGroup = challengeGroupRepository.findById(groupId)
        .map(ChallengeGroup::fromEntity)
        .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다."));

    if (!isChallengeGroupMember(challengeGroup, member)) {
      throw new IllegalArgumentException("그룹에 속해 있지 않습니다.");
    }

    if (challengeGroup.isLinkValid()) {
      return InviteLinkResponseDto.fromEntity(challengeGroup.toEntity());
    }

    int INVITE_LINK_LENGTH = 10;
    String inviteLink = RandomStringGenerator.generateRandomString(INVITE_LINK_LENGTH);
    ChallengeGroupEntity challengeGroupEntity = challengeGroup.updateInviteLink(inviteLink).toEntity();

    return InviteLinkResponseDto.fromEntity(
        challengeGroupRepository.save(challengeGroupEntity));
  }

  public ChallengeGroupResponseDto createChallengeGroup(ChallengeGroupRequestDto challengeGroupRequestDto,
      Member member) {
    ChallengeGroupEntity challengeGroupEntity = challengeGroupRepository.save(
        ChallengeGroup.fromRequest(challengeGroupRequestDto, member).toEntity());

    memberGroupRepository.save(MemberGroup.create(ChallengeGroup.fromEntity(challengeGroupEntity), member).toEntity());
    return ChallengeGroupResponseDto.fromEntity(challengeGroupEntity);
  }

  public ChallengeGroupResponseDto joinChallengeGroup(String inviteLink, Member member) {
    ChallengeGroup challengeGroup = challengeGroupRepository.findByInviteLink(inviteLink)
        .map(ChallengeGroup::fromEntity)
        .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다."));

    if (isChallengeGroupMember(challengeGroup, member)) {
      throw new IllegalArgumentException("이미 그룹에 속해있습니다.");
    }

    Message enterMessage = Message.createEnterMessage(challengeGroup, member);

    messageService.saveAndSend(enterMessage);

    MemberGroup memberGroup = MemberGroup.create(challengeGroup, member);
    memberGroupRepository.save(memberGroup.toEntity());

    return ChallengeGroupResponseDto.fromEntity(challengeGroup.toEntity());
  }

  public void leaveChallengeGroup(Long groupId, Long leaveMemberId, Member member) {
    ChallengeGroup challengeGroup = challengeGroupRepository.findById(groupId)
        .map(ChallengeGroup::fromEntity)
        .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다."));

    if (challengeGroup.isAdmin(member)) {
      MemberEntity leaveMember = memberRepository.findById(leaveMemberId)
          .orElseThrow(() -> new IllegalArgumentException("멤버가 존재하지 않습니다."));

      memberGroupRepository.findByMemberAndChallengeGroup(leaveMember, challengeGroup.toEntity())
          .ifPresent(memberGroupRepository::delete);
    } else if (leaveMemberId.equals(member.getId())) {
      memberGroupRepository.findByMemberAndChallengeGroup(member.toEntity(), challengeGroup.toEntity())
          .ifPresent(memberGroupRepository::delete);
    } else {
      throw new IllegalArgumentException("그룹에 속해 있는 사람만 그룹을 나갈 수 있습니다.");
    }

    Message leaveMessage = Message.createLeaveMessage(challengeGroup, member);
    messageService.saveAndSend(leaveMessage);
  }

  public boolean isChallengeGroupMember(ChallengeGroup challengeGroup, Member member) {
    if (challengeGroup.isAdmin(member)) {
      return true;
    }

    List<Long> memberIds = memberGroupRepository.findByChallengeGroup(challengeGroup.toEntity())
        .stream()
        .map(memberGroup -> memberGroup.getMember().getId())
        .toList();

    return memberIds.contains(member.getId());
  }

  public List<ChallengeGroupResponseDto> getChallengeGroups(Member member) {
    memberService.validateAndGetMember(member);
    List<ChallengeGroupEntity> challengeGroupEntities = memberGroupRepository.findByMember(member.toEntity())
        .stream()
        .map(MemberGroupEntity::getChallengeGroup)
        .toList();

    return ChallengeGroupResponseDto.fromEntities(challengeGroupEntities);
  }

  public ChallengeGroupResponseDto updateChallengeGroup(Long groupId, ChallengeGroupRequestDto challengeGroupRequestDto,
      Member member) {
    ChallengeGroup challengeGroup = challengeGroupRepository.findById(groupId)
        .map(ChallengeGroup::fromEntity)
        .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다."));

    if (!challengeGroup.isAdmin(member)) {
      throw new IllegalArgumentException("그룹장이 수정 할 수 있습니다.");
    }

    return ChallengeGroupResponseDto.fromEntity(
        challengeGroupRepository.save(challengeGroup.update(challengeGroupRequestDto).toEntity()));
  }

  //TODO: 그룹 삭제시 MemberGroup이 기록은 남아있되 사용은 못하도록 하기
  public Long deleteChallengeGroup(Long groupId, Member member) {
    ChallengeGroup challengeGroup = challengeGroupRepository.findById(groupId)
        .map(ChallengeGroup::fromEntity)
        .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다."));

    if (!challengeGroup.isAdmin(member)) {
      throw new IllegalArgumentException("그룹장이 삭제 할 수 있습니다.");
    }

    challengeGroupRepository.delete(challengeGroup.toEntity());
    return groupId;
  }

  public MemberGroupResponseDto saveMoney(Long groupId, SavingMoneyRequestDto savingMoneyRequestDto, Member member) {
    ChallengeGroup challengeGroup = challengeGroupRepository.findById(groupId)
        .map(ChallengeGroup::fromEntity)
        .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다."));

    MemberGroup memberGroup = memberGroupRepository.findByMemberAndChallengeGroup(member.toEntity(),
            challengeGroup.toEntity())
        .map(MemberGroup::fromEntity)
        .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않습니다."));

    memberGroupRepository.save(memberGroup.updateSavedAmount(savingMoneyRequestDto).toEntity());

    return MemberGroupResponseDto.fromEntity(memberGroup.toEntity());
  }

  public List<Message> getMessages(Long groupId, Member member) {
    ChallengeGroupEntity challengeGroupEntity = challengeGroupRepository.findById(groupId)
        .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다."));

    if (!isChallengeGroupMember(ChallengeGroup.fromEntity(challengeGroupEntity), member)) {
      throw new IllegalArgumentException("그룹에 속해 있지 않습니다.");
    }
    List<MessageEntity> messageEntities = messageRepository.findByGroup(challengeGroupEntity);
    return Message.fromEntities(messageEntities);
  }
}
