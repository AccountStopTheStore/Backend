package com.cozybinarybase.accountstopthestore.model.member.controller;

import com.cozybinarybase.accountstopthestore.model.accountbook.persist.repository.AccountBookRepository;
import com.cozybinarybase.accountstopthestore.model.asset.persist.repository.AssetRepository;
import com.cozybinarybase.accountstopthestore.model.category.persist.repository.CategoryRepository;
import com.cozybinarybase.accountstopthestore.model.member.domain.Member;
import com.cozybinarybase.accountstopthestore.model.member.dto.EmailSignUpResponseDto;
import com.cozybinarybase.accountstopthestore.model.member.dto.EmailSignInRequestDto;
import com.cozybinarybase.accountstopthestore.model.member.dto.EmailSignUpRequestDto;
import com.cozybinarybase.accountstopthestore.model.member.dto.WithdrawalResponseDto;
import com.cozybinarybase.accountstopthestore.model.member.service.MemberService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    memberService.signInWithEmail(emailSignInRequestDto);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/withdrawal")
  public ResponseEntity<?> signOut(@AuthenticationPrincipal Member member) {
    WithdrawalResponseDto response = memberService.withdrawal(member);

    return ResponseEntity.ok(response);
  }
}
