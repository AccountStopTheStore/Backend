package com.cozybinarybase.accountstopthestore.model.asset.dto.constants;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AssetGroup {
  MONEY("현금"),
  BANK("은행"),
  CARD("카드");

  private final String value;

  @JsonValue
  public String toValue() {
    return this.value;
  }
}
