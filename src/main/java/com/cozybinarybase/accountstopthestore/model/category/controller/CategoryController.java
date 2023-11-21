package com.cozybinarybase.accountstopthestore.model.category.controller;

import com.cozybinarybase.accountstopthestore.model.category.dto.CategoryResponseDto;
import com.cozybinarybase.accountstopthestore.model.category.dto.CategorySaveRequestDto;
import com.cozybinarybase.accountstopthestore.model.category.dto.CategoryUpdateRequestDto;
import com.cozybinarybase.accountstopthestore.model.category.service.CategoryService;
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

@RequiredArgsConstructor
@RequestMapping("/categories")
@RestController
public class CategoryController {

  private final CategoryService categoryService;

  @Operation(summary = "카테고리 추가", description = "유저가 카테고리를 추가할 때 사용되는 API")
  @PostMapping
  public ResponseEntity<?> saveCategory(
      @RequestBody @Valid CategorySaveRequestDto requestDto,
      @AuthenticationPrincipal Member member
  ) {
    CategoryResponseDto responseDto = categoryService.saveCategory(requestDto, member);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  @Operation(summary = "카테고리 수정", description = "유저가 카테고리를 수정할 때 사용되는 API")
  @PutMapping("/{categoryId}")
  public ResponseEntity<?> updateCategory(
      @PathVariable Long categoryId,
      @RequestBody @Valid CategoryUpdateRequestDto requestDto,
      @AuthenticationPrincipal Member member
  ) {
    CategoryResponseDto responseDto =
        categoryService.updateCategory(categoryId, requestDto, member);
    return ResponseEntity.ok().body(responseDto);
  }

  @Operation(summary = "카테고리 삭제", description = "유저가 카테고리를 삭제할 때 사용되는 API")
  @DeleteMapping("/{categoryId}")
  public ResponseEntity<?> deleteCategory(
      @PathVariable Long categoryId,
      @AuthenticationPrincipal Member member
  ) {
    categoryService.deleteCategory(categoryId, member);
    return ResponseEntity.ok().body("카테고리가 삭제되었습니다.");
  }

  @Operation(summary = "카테고리 목록 조회", description = "유저가 카테고리 목록을 조회할 때 사용되는 API")
  @GetMapping
  public ResponseEntity<?> getAllCategories(
      @AuthenticationPrincipal Member member
  ) {
    List<CategoryResponseDto> responseDtoList = categoryService.getAllCategories(member);
    return ResponseEntity.ok().body(responseDtoList);
  }
}
