package com.cozybinarybase.accountstopthestore.model.message.service;

import com.cozybinarybase.accountstopthestore.model.challenge.persist.entity.ChallengeGroupEntity;
import com.cozybinarybase.accountstopthestore.model.challenge.persist.repository.ChallengeGroupRepository;
import com.cozybinarybase.accountstopthestore.model.challenge.persist.repository.MemberGroupRepository;
import com.cozybinarybase.accountstopthestore.model.member.domain.Member;
import com.cozybinarybase.accountstopthestore.model.message.domain.Message;
import com.cozybinarybase.accountstopthestore.model.message.persist.entity.MessageEntity;
import com.cozybinarybase.accountstopthestore.model.message.persist.repository.MessageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

  private final MessageRepository messageRepository;
  private final MemberGroupRepository memberGroupRepository;
  private final ChallengeGroupRepository challengeGroupRepository;

  public void save(Message message) {
    messageRepository.save(message.toEntity());
  }

  public List<Message> getMessages(Long groupId, Member member) {
    ChallengeGroupEntity challengeGroupEntity = challengeGroupRepository.findById(groupId)
        .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다."));

    if (!memberGroupRepository.existsByMemberAndChallengeGroup(member.toEntity(), challengeGroupEntity)) {
      throw new IllegalArgumentException("그룹에 속해 있지 않습니다.");
    }
    List<MessageEntity> messageEntities = messageRepository.findByChallengeGroup(challengeGroupEntity);
    return Message.fromEntities(messageEntities);
  }
}
