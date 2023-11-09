package com.cozybinarybase.accountstopthestore.model.message.domain;

import com.cozybinarybase.accountstopthestore.model.challenge.persist.entity.ChallengeGroupEntity;
import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
import com.cozybinarybase.accountstopthestore.model.message.dto.constants.MessageType;
import com.cozybinarybase.accountstopthestore.model.message.persist.entity.MessageEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {

  private Long id;
  private Long groupId; // 그 번호
  private Long senderId; // 채팅을 보낸 사람
  private String message; // 메시지
  private MessageType messageType;

  public static Message fromEntity(MessageEntity messageEntity) {
    return Message.builder()
        .id(messageEntity.getId())
        .groupId(messageEntity.getGroup().getId())
        .senderId(messageEntity.getSender().getId())
        .message(messageEntity.getMessage())
        .messageType(messageEntity.getMessageType())
        .build();
  }

  public static List<Message> fromEntities(List<MessageEntity> messageEntities) {
    return messageEntities.stream()
        .map(Message::fromEntity)
        .toList();
  }

  public MessageEntity toEntity() {
    return MessageEntity.builder()
        .group(ChallengeGroupEntity.builder().id(groupId).build())
        .sender(MemberEntity.builder().id(senderId).build())
        .message(message)
        .messageType(messageType)
        .build();
  }
}
