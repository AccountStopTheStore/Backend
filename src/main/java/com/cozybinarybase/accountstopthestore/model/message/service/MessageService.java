package com.cozybinarybase.accountstopthestore.model.message.service;

import com.cozybinarybase.accountstopthestore.model.member.domain.Member;
import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
import com.cozybinarybase.accountstopthestore.model.member.service.MemberService;
import com.cozybinarybase.accountstopthestore.model.message.domain.Message;
import com.cozybinarybase.accountstopthestore.model.message.persist.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

  private final MessageRepository messageRepository;
  private final SimpMessagingTemplate messagingTemplate;
  private final MemberService memberService;

  public void saveAndSend(Message message, Member member) {
    MemberEntity memberEntity = memberService.validateAndGetMember(member);
    message.setSenderId(memberEntity.getId());
    messageRepository.save(message.toEntity());
    messagingTemplate.convertAndSend("/chat/groups/" + message.getGroupId(), message);
  }
}
