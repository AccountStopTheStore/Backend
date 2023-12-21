package com.cozybinarybase.accountstopthestore.security;


import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
import com.cozybinarybase.accountstopthestore.model.member.persist.repository.MemberRepository;
import com.cozybinarybase.accountstopthestore.security.oauth2.CustomOAuth2User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

  public static final String ACCESS_TOKEN_HEADER = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";
  private static final String REFRESH_TOKEN_HEADER = "Authorization-refresh";
  
  private final MemberRepository memberRepository;

  @Value("${jwt.secretKey}")
  private String secretKey;

  @Value("${jwt.access.expiration}")
  private Long accessTokenExpirationPeriod;

  @Value("${jwt.refresh.expiration}")
  private Long refreshTokenExpirationPeriod;

  private JwtParser jwtParser;

  private synchronized JwtParser getJwtParser() {
    if (this.jwtParser == null) {
      this.jwtParser = Jwts.parser().setSigningKey(this.secretKey);
    }
    return this.jwtParser;
  }

  private String generateAccessToken(String email) {
    Claims claims = Jwts.claims().setSubject(email);

    Date now = new Date();
    Date expireDate = new Date(now.getTime() + accessTokenExpirationPeriod);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expireDate)
        .signWith(SignatureAlgorithm.HS512, this.secretKey) // 사용할 암호화 알고리즘, 비밀키
        .compact();
  }

  public String generateAccessToken(UserDetails userDetails) {
    return this.generateAccessToken(userDetails.getUsername());
  }

  public String generateAccessToken(CustomOAuth2User customOAuth2User) {
    return this.generateAccessToken((String) customOAuth2User.getAttributes().get("email"));
  }

  public String generateRefreshToken() {
    Date now = new Date();
    Date expireDate = new Date(now.getTime() + refreshTokenExpirationPeriod);
    // jwt 발급
    return Jwts.builder()
        .setExpiration(expireDate)
        .signWith(SignatureAlgorithm.HS512, this.secretKey)
        .compact();
  }

  public String getUsername(String token) {
    return getJwtParser().parseClaimsJws(token).getBody().getSubject();
  }

  public long getRefreshTokenRemainingTime(String refreshToken) {
    Claims claims = getJwtParser().parseClaimsJws(refreshToken).getBody();
    Date expiration = claims.getExpiration();
    Date now = new Date();
    long remainingTimeMillis = expiration.getTime() - now.getTime();
    return remainingTimeMillis / 1000;
  }

  public boolean validateToken(String token) {
    try {
      Jws<Claims> claims = getJwtParser().parseClaimsJws(token);

      return !claims.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
    ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
        .httpOnly(true)
        .path("/")
        .sameSite("None")
        .secure(true)
        .build();

    ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
        .httpOnly(true)
        .path("/")
        .sameSite("None")
        .secure(true)
        .build();

    response.addHeader("Set-Cookie", accessTokenCookie.toString());
    response.addHeader("Set-Cookie", refreshTokenCookie.toString());

    log.info("Access Token, Refresh Token 쿠키 설정 완료");

    MemberEntity memberEntity = memberRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new IllegalStateException("Member not found"));

    JSONObject userInfoJson = new JSONObject();
    userInfoJson.put("id", memberEntity.getId());
    userInfoJson.put("email", memberEntity.getEmail());
    userInfoJson.put("name", memberEntity.getName());
    userInfoJson.put("refreshTokenRemainingTime", getRefreshTokenRemainingTime(refreshToken));

    String userInfoString = Base64.getEncoder().encodeToString(userInfoJson.toString().getBytes());

    ResponseCookie userInfoCookie = ResponseCookie.from("userInfo", userInfoString)
        .httpOnly(false)
        .path("/")
        .sameSite("None")
        .secure(false)
        .build();

    response.addHeader("Set-Cookie", userInfoCookie.toString());

    log.info("사용자 정보 쿠키 설정 완료");
  }

  public Optional<String> extractToken(HttpServletRequest request, String cookieName) {
    if (request.getCookies() == null) {
      return Optional.empty();
    }
    return Arrays.stream(request.getCookies())
        .filter(cookie -> cookie.getName().equals(cookieName))
        .findFirst()
        .map(Cookie::getValue);
  }

  public Optional<String> extractRefreshToken(HttpServletRequest request) {
    return extractToken(request, "refreshToken");
  }

  public Optional<String> extractAccessToken(HttpServletRequest request) {
    return extractToken(request, "accessToken");
  }
}
