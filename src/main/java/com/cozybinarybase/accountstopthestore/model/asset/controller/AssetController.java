package com.cozybinarybase.accountstopthestore.model.asset.controller;

import com.cozybinarybase.accountstopthestore.model.asset.dto.AssetResponseDto;
import com.cozybinarybase.accountstopthestore.model.asset.dto.AssetSaveRequestDto;
import com.cozybinarybase.accountstopthestore.model.asset.dto.AssetUpdateRequestDto;
import com.cozybinarybase.accountstopthestore.model.asset.service.AssetService;
import com.cozybinarybase.accountstopthestore.model.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Tag(name = "Asset API", description = "자산 관련 API 정보를 담고 있습니다.")
@RequiredArgsConstructor
@RequestMapping("/assets")
@RestController
public class AssetController {

  private final AssetService assetService;

  @Operation(summary = "자산 추가", description = "유저가 자산을 추가할 때 사용되는 API")
  @PostMapping
  public ResponseEntity<AssetResponseDto> saveAsset(
      @RequestBody @Valid AssetSaveRequestDto requestDto,
      @AuthenticationPrincipal Member member
  ) {
    AssetResponseDto responseDto = assetService.saveAsset(requestDto, member);

    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  @Operation(summary = "자산 수정", description = "유저가 자산을 수정할 때 사용되는 API")
  @PutMapping("/{assetId}")
  public ResponseEntity<AssetResponseDto> updateAsset(
      @PathVariable Long assetId,
      @RequestBody @Valid AssetUpdateRequestDto requestDto,
      @AuthenticationPrincipal Member member
  ) {
    AssetResponseDto responseDto = assetService.updateAsset(assetId, requestDto, member);

    return ResponseEntity.ok(responseDto);
  }

  @Operation(summary = "자산 삭제", description = "유저가 자산을 삭제할 때 사용되는 API")
  @DeleteMapping("/{assetId}")
  public ResponseEntity<String> deleteAsset(
      @PathVariable Long assetId,
      @AuthenticationPrincipal Member member
  ) {
    assetService.deleteAsset(assetId, member);

    return ResponseEntity.ok().body("자산이 삭제되었습니다.");
  }

  @Operation(summary = "자산 목록 조회", description = "유저가 자산 목록을 조회할 때 사용되는 API")
  @GetMapping
  public ResponseEntity<List<AssetResponseDto>> getAllAssets(
      @AuthenticationPrincipal Member member
  ) {
    List<AssetResponseDto> responseDtoList = assetService.getAllAssets(member);
    return ResponseEntity.ok().body(responseDtoList);
  }
}
