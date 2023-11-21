package com.cozybinarybase.accountstopthestore.model.images.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OcrResultDto {
  private String date;
  private Long amount;
  private String vendor;
  private String address;
}