package com.cozybinarybase.accountstopthestore.model.asset.dto.constants;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AssetType {

  MONEY("현금"),
  KB_KOOKMIN_BANK("국민은행"),
  SHINHAN_BANK("신한은행"),
  WOORI_BANK("우리은행"),
  HANA_BANK("하나은행"),
  IBK_BANK("기업은행"),
  NH_NONGHYUP_BANK("농협은행"),
  SHINHAN_CARD("신한카드"),
  HYUNDAI_CARD("현대카드"),
  SAMSUNG_CARD("삼성카드"),
  KB_KOOKMIN_CARD("KB국민카드"),
  NH_NONGHYUP_CARD("NH농협카드"),
  WOORI_CARD("우리카드"),
  LOTTE_CARD("롯데카드"),
  BC_CARD("비씨카드"),
  HANA_CARD("하나카드");

  private final String value;

  @JsonValue
  public String toValue() {
    return this.value;
  }
}
