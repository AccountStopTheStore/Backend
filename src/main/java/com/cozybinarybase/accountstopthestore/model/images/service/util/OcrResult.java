package com.cozybinarybase.accountstopthestore.model.images.service.util;

import lombok.Data;

@Data
public class OcrResult {
  private String ocrDate;
  private Long ocrAmount;
  private String ocrVendor;
  private String ocrAddress;
}
