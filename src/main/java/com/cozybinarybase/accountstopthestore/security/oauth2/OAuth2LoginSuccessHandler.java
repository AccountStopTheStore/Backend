package com.cozybinarybase.accountstopthestore.security.oauth2;

import com.cozybinarybase.accountstopthestore.model.member.dto.constants.AuthType;
import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
import com.cozybinarybase.accountstopthestore.model.member.persist.repository.MemberRepository;
import com.cozybinarybase.accountstopthestore.security.TokenProvider;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final TokenProvider tokenProvider;
  private final MemberRepository memberRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
    String email = (String) customOAuth2User.getAttributes().get("email");
    MemberEntity member = memberRepository.findByEmail(email)
        .orElseGet(() -> memberRepository.save(customOAuth2User.toEntity()));

    if (member.getAuthType() == AuthType.EMAIL) {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write("{\"message\": \"이메일 가입자입니다.\"}");
      response.setStatus(HttpServletResponse.SC_OK);
      //TODO: 클라이언트 쪽에서 처음 페이지 받기
      response.sendRedirect("https://asts.cozybinarybase.com:8443/login");
      return;
    }

    String accessToken = this.tokenProvider.generateAccessToken(customOAuth2User);
    String refreshToken = this.tokenProvider.generateRefreshToken();
    this.tokenProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    response.sendRedirect("https://asts.cozybinarybase.com:8443/account");
  }
}
