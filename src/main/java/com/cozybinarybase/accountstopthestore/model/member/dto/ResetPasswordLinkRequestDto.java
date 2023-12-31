package com.cozybinarybase.accountstopthestore.model.member.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordLinkRequestDto {
  @NotBlank(message = "이메일 주소가 필요합니다.")
  private String email;
}
