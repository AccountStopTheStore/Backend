package com.cozybinarybase.accountstopthestore.common.handler;

import com.cozybinarybase.accountstopthestore.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.WebUtils;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

  private final TokenProvider tokenProvider;

  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
    if (request instanceof ServletServerHttpRequest) {
      ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
      HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
      Cookie jwtCookie = WebUtils.getCookie(httpServletRequest, "accessToken");

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (jwtCookie != null) {
        String jwt = jwtCookie.getValue();
        if (authentication != null && authentication.isAuthenticated()) {
          attributes.put("member", authentication);
        }
        if (tokenProvider.validateToken(jwt)) {
          String username = tokenProvider.getUsername(jwt);
          attributes.put("email", username); // 저장된 사용자 이름을 웹소켓 세션의 속성으로 추가
        } else {
          return false; // 토큰이 유효하지 않으면 핸드셰이크를 거부
        }
      } else {
        return false; // 쿠키가 없으면 핸드셰이크를 거부
      }
    }
    return true;
  }

  @Override
  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Exception exception) {
  }
}
