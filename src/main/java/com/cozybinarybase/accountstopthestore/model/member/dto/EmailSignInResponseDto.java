package com.cozybinarybase.accountstopthestore.model.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailSignInResponseDto {
  String accessToken;
  String refreshToken;
}
