package com.cozybinarybase.accountstopthestore.model.message.controller;

import com.cozybinarybase.accountstopthestore.model.member.domain.Member;
import com.cozybinarybase.accountstopthestore.model.message.domain.Message;
import com.cozybinarybase.accountstopthestore.model.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class MessageController {

  private final MessageService messageService;

  @MessageMapping("/send")
  public void chat(Message message,
      SimpMessageHeaderAccessor headerAccessor) {
    Authentication authentication = (Authentication) headerAccessor.getSessionAttributes().get("member");
    Member member = (Member) authentication.getPrincipal();
    messageService.saveAndSend(message, member);
  }
}
