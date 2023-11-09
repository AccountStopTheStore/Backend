package com.cozybinarybase.accountstopthestore.security;


import com.cozybinarybase.accountstopthestore.model.member.domain.Member;
import com.cozybinarybase.accountstopthestore.model.member.persist.repository.MemberRepository;
import com.cozybinarybase.accountstopthestore.security.oauth2.CustomOAuth2User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

  public static final String TOKEN_HEADER = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";
  private static final String REFRESH_TOKEN_HEADER = "Authorization-refresh";
  
  private final MemberRepository memberRepository;

  @Value("${jwt.secretKey}")
  private String secretKey;

  @Value("${jwt.access.expiration}")
  private Long accessTokenExpirationPeriod;

  @Value("${jwt.refresh.expiration}")
  private Long refreshTokenExpirationPeriod;

  private String generateAccessToken(String email) {
    Claims claims = Jwts.claims().setSubject(email);

    Date now = new Date();
    Date expireDate = new Date(now.getTime() + accessTokenExpirationPeriod);

    // jwt 발급
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
    return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token);

      return !claims.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  public void updateRefreshToken(String email, String refreshToken) {
    Member member = Member.fromEntity(
        this.memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다.")));
    member.updateRefreshToken(refreshToken);
    this.memberRepository.save(member.toEntity());
  }

  public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
    response.setStatus(HttpServletResponse.SC_OK);

    setAccessTokenHeader(response, accessToken);
    setRefreshTokenHeader(response, refreshToken);
    log.info("Access Token, Refresh Token 헤더 설정 완료");
  }

  public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
    response.setHeader(TOKEN_HEADER, TOKEN_PREFIX + accessToken);
  }

  public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
    response.setHeader(REFRESH_TOKEN_HEADER, TOKEN_PREFIX + refreshToken);
  }

  public Optional<String> extractRefreshToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(REFRESH_TOKEN_HEADER))
        .filter(refreshToken -> refreshToken.startsWith(TOKEN_PREFIX))
        .map(refreshToken -> refreshToken.replace(TOKEN_PREFIX, ""));
  }

  public Optional<String> extractAccessToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(TOKEN_HEADER))
        .filter(refreshToken -> refreshToken.startsWith(TOKEN_PREFIX))
        .map(refreshToken -> refreshToken.replace(TOKEN_PREFIX, ""));
  }

}
