package com.cozybinarybase.accountstopthestore.model.asset.dto;

import com.cozybinarybase.accountstopthestore.model.asset.dto.constants.AssetType;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AssetSaveRequestDto {

  private AssetType assetType;

  @NotBlank(message = "자산명을 입력해주시길 바랍니다.")
  private String assetName;

  private Long amount;

  private String memo;
}
