package com.cozybinarybase.accountstopthestore.model.category.service;

import com.cozybinarybase.accountstopthestore.model.category.domain.Category;
import com.cozybinarybase.accountstopthestore.model.category.dto.CategoryResponseDto;
import com.cozybinarybase.accountstopthestore.model.category.dto.CategorySaveRequestDto;
import com.cozybinarybase.accountstopthestore.model.category.dto.CategoryUpdateRequestDto;
import com.cozybinarybase.accountstopthestore.model.category.dto.constants.CategoryType;
import com.cozybinarybase.accountstopthestore.model.category.exception.CategoryNotValidException;
import com.cozybinarybase.accountstopthestore.model.category.persist.entity.CategoryEntity;
import com.cozybinarybase.accountstopthestore.model.category.persist.repository.CategoryRepository;
import com.cozybinarybase.accountstopthestore.model.member.domain.Member;
import com.cozybinarybase.accountstopthestore.model.member.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final MemberService memberService;
  private final Category category;

  @Transactional
  public CategoryResponseDto saveCategory(
      CategorySaveRequestDto requestDto, Member member
  ) {
    memberService.validateAndGetMember(member);

    existCategoryOfMember(
        requestDto.getName(), requestDto.getType(), member.getId());

    CategoryEntity categoryEntity =
        categoryRepository.save(category.createCategory(requestDto, member.getId()).toEntity());
    return CategoryResponseDto.fromEntity(categoryEntity);
  }

  @Transactional
  public CategoryResponseDto updateCategory(
      Long categoryId,
      CategoryUpdateRequestDto requestDto,
      Member member
  ) {
    memberService.validateAndGetMember(member);

    CategoryEntity categoryEntity = categoryRepository.findById(categoryId).orElseThrow(
        CategoryNotValidException::new
    );

    existCategoryOfMember(
        requestDto.getName(), requestDto.getType(), member.getId());

    Category categoryDomain = Category.fromEntity(categoryEntity);
    categoryDomain.updateCategory(requestDto);

    CategoryEntity updateCategoryEntity = categoryDomain.toEntity();
    categoryRepository.save(updateCategoryEntity);

    return CategoryResponseDto.fromEntity(updateCategoryEntity);
  }

  @Transactional
  public void deleteCategory(Long categoryId, Member member) {
    memberService.validateAndGetMember(member);

    CategoryEntity categoryEntity = categoryRepository.findById(categoryId).orElseThrow(
        CategoryNotValidException::new
    );

    categoryRepository.delete(categoryEntity);
  }

  @Transactional(readOnly = true)
  public List<CategoryResponseDto> getAllCategories(Member member) {
    memberService.validateAndGetMember(member);

    List<CategoryEntity> categoryEntities = categoryRepository.findByMember(member.getId());

    return CategoryResponseDto.fromEntities(categoryEntities);
  }

  private void existCategoryOfMember(String categoryName, CategoryType categoryType,
      Long memberId) {
    if (categoryRepository.existsByNameAndTypeAndMember(
        categoryName, categoryType, memberId)
    ) {
      throw new CategoryNotValidException("이미 존재하는 카테고리입니다.");
    }
  }
}
