package com.cozybinarybase.accountstopthestore.security.oauth2;

import com.cozybinarybase.accountstopthestore.model.member.dto.constants.Authority;
import com.cozybinarybase.accountstopthestore.model.member.persist.repository.MemberRepository;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final MemberRepository memberRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
        .getUserInfoEndpoint().getUserNameAttributeName();

    Map<String, Object> attributes = oAuth2User.getAttributes();

    CustomOAuth2User customOAuth2User = new CustomOAuth2User(
        Collections.singleton(new SimpleGrantedAuthority(Authority.USER.name())),
        attributes,
        userNameAttributeName
    );

//    String email = (String) attributes.get("email");
//    MemberEntity member = memberRepository.findByEmail(email)
//        .orElseGet(() -> memberRepository.save(
//            customOAuth2User.toEntity()
//        ));

    return customOAuth2User;
  }
}