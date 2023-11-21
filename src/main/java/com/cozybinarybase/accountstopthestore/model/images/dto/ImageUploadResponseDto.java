package com.cozybinarybase.accountstopthestore.model.images.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageUploadResponseDto {
  private Long imageId;
  private OcrResultDto ocrResult;
}
