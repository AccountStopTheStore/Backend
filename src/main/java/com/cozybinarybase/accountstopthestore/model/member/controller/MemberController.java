package com.cozybinarybase.accountstopthestore.model.member.controller;

import com.cozybinarybase.accountstopthestore.model.member.dto.EmailSignInResponseDto;
import com.cozybinarybase.accountstopthestore.model.member.dto.EmailSignUpResponseDto;
import com.cozybinarybase.accountstopthestore.model.member.dto.EmailSignInRequestDto;
import com.cozybinarybase.accountstopthestore.model.member.dto.EmailSignUpRequestDto;
import com.cozybinarybase.accountstopthestore.model.member.service.MemberService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/sign-up/email")
  public ResponseEntity<?> signUpWithEmail(@RequestBody @Valid EmailSignUpRequestDto memberSignUpRequest) {
    EmailSignUpResponseDto response = memberService.signUpWithEmail(memberSignUpRequest);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/sign-in/email")
  public ResponseEntity<?> signInWithEmail(@RequestBody @Valid EmailSignInRequestDto emailSignInRequestDto) {
    EmailSignInResponseDto response = memberService.signInWithEmail(emailSignInRequestDto);
    return ResponseEntity.ok(response);
  }
}
