package com.cozybinarybase.accountstopthestore.model.asset.controller;

import com.cozybinarybase.accountstopthestore.common.dto.ResponseDto;
import com.cozybinarybase.accountstopthestore.model.asset.dto.AssetDeleteResponseDto;
import com.cozybinarybase.accountstopthestore.model.asset.dto.AssetResponseDto;
import com.cozybinarybase.accountstopthestore.model.asset.dto.AssetSaveRequestDto;
import com.cozybinarybase.accountstopthestore.model.asset.dto.AssetSaveResponseDto;
import com.cozybinarybase.accountstopthestore.model.asset.dto.AssetSearchTypeListResponseDto;
import com.cozybinarybase.accountstopthestore.model.asset.dto.AssetUpdateRequestDto;
import com.cozybinarybase.accountstopthestore.model.asset.dto.AssetUpdateResponseDto;
import com.cozybinarybase.accountstopthestore.model.asset.dto.constants.AssetType;
import com.cozybinarybase.accountstopthestore.model.asset.service.AssetService;
import com.cozybinarybase.accountstopthestore.model.member.domain.Member;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class AssetController {

  private final AssetService assetService;

  @PostMapping("/{memberId}/assets")
  public ResponseEntity<?> saveAsset(
      @PathVariable Long memberId,
      @RequestBody @Valid AssetSaveRequestDto requestDto,
      BindingResult bindingResult,
      @AuthenticationPrincipal Member member
  ) {
    AssetSaveResponseDto responseDto = assetService.saveAsset(memberId, requestDto, member);

    return new ResponseEntity<>(
        new ResponseDto<>(true, "자산 추가", responseDto), HttpStatus.CREATED
    );
  }

  @PutMapping("/{memberId}/assets/{assetId}")
  public ResponseEntity<?> updateAsset(
      @PathVariable Long memberId,
      @PathVariable Long assetId,
      @RequestBody @Valid AssetUpdateRequestDto requestDto,
      BindingResult bindingResult,
      @AuthenticationPrincipal Member member
  ) {
    AssetUpdateResponseDto responseDto =
        assetService.updateAsset(memberId, assetId, requestDto, member);

    return new ResponseEntity<>(
        new ResponseDto<>(true, "자산 수정", responseDto), HttpStatus.OK
    );
  }

  @DeleteMapping("/{memberId}/assets/{assetId}")
  public ResponseEntity<?> deleteAsset(
      @PathVariable Long memberId,
      @PathVariable Long assetId,
      @AuthenticationPrincipal Member member
  ) {
    AssetDeleteResponseDto responseDto =
        assetService.deleteAsset(memberId, assetId, member);

    return new ResponseEntity<>(
        new ResponseDto<>(true, "자산 삭제", responseDto), HttpStatus.OK
    );
  }

  @GetMapping("/{memberId}/assets/{assetId}")
  public ResponseEntity<?> getAsset(
      @PathVariable Long memberId,
      @PathVariable Long assetId,
      @AuthenticationPrincipal Member member
  ) {
    AssetResponseDto responseDto =
        assetService.getAsset(memberId, assetId, member);

    return new ResponseEntity<>(
        new ResponseDto<>(true, "자산 상세 조회", responseDto), HttpStatus.OK
    );
  }

  @GetMapping("/{memberId}/assets")
  public ResponseEntity<?> searchAssetType(
      @PathVariable Long memberId,
      @RequestParam AssetType assetType,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int limit,
      @AuthenticationPrincipal Member member
  ) {
    AssetSearchTypeListResponseDto responseDto =
        assetService.searchAssetType(memberId, assetType, page, limit, member);

    return new ResponseEntity<>(
        new ResponseDto<>(true, "자산 조회", responseDto), HttpStatus.OK
    );
  }
}
