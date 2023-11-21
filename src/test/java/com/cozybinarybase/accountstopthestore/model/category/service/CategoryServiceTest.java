package com.cozybinarybase.accountstopthestore.model.category.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cozybinarybase.accountstopthestore.model.category.domain.Category;
import com.cozybinarybase.accountstopthestore.model.category.dto.CategoryResponseDto;
import com.cozybinarybase.accountstopthestore.model.category.dto.CategorySaveRequestDto;
import com.cozybinarybase.accountstopthestore.model.category.dto.CategoryUpdateRequestDto;
import com.cozybinarybase.accountstopthestore.model.category.dto.constants.CategoryType;
import com.cozybinarybase.accountstopthestore.model.category.persist.entity.CategoryEntity;
import com.cozybinarybase.accountstopthestore.model.category.persist.repository.CategoryRepository;
import com.cozybinarybase.accountstopthestore.model.member.domain.Member;
import com.cozybinarybase.accountstopthestore.model.member.dto.constants.Authority;
import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
import com.cozybinarybase.accountstopthestore.model.member.service.MemberService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

  @InjectMocks
  private CategoryService categoryService;

  @Mock
  private MemberService memberService;

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private Category categoryMock;

  @Test
  void 카테고리_생성_test() throws Exception {
    // given
    CategorySaveRequestDto requestDto = new CategorySaveRequestDto();
    requestDto.setName("월급");
    requestDto.setType(CategoryType.INCOME);

    MemberEntity member = new MemberEntity();
    member.setId(1L);
    member.setRole(Authority.USER);
    member.setEmail("test@test.com");
    member.setPassword("1234");
    member.setName("홍길동");
    Member loginMember = Member.fromEntity(member);

    CategoryEntity savedCategory = new CategoryEntity();
    savedCategory.setId(1L);
    savedCategory.setName("월급");
    savedCategory.setType(CategoryType.INCOME);
    savedCategory.setMember(member);

    Category categoryDomain = Category.builder()
        .name(requestDto.getName())
        .type(requestDto.getType())
        .memberId(1L)
        .build();

    // stub 1
    when(memberService.validateAndGetMember(loginMember)).thenReturn(member);

    // stub 2
    when(categoryRepository.existsByNameAndTypeAndMember(any(), any(), any())).thenReturn(false);

    // stub 3
    when(categoryMock.createCategory(requestDto, 1L)).thenReturn(categoryDomain);

    // stub 4
    when(categoryRepository.save(any())).thenReturn(savedCategory);

    // when
    CategoryResponseDto responseDto = categoryService.saveCategory(requestDto, loginMember);

    // then
    assertEquals("월급", responseDto.getName());
    assertEquals("수입", responseDto.getType());
  }

  @Test
  void 카테고리_수정_test() throws Exception {
    // given
    CategoryUpdateRequestDto requestDto = new CategoryUpdateRequestDto();
    requestDto.setName("적금");
    requestDto.setType(CategoryType.SPENDING);

    MemberEntity member = new MemberEntity();
    member.setId(1L);
    member.setRole(Authority.USER);
    member.setEmail("test@test.com");
    member.setPassword("1234");
    member.setName("홍길동");
    Member loginMember = Member.fromEntity(member);

    CategoryEntity savedCategory = new CategoryEntity();
    savedCategory.setId(1L);
    savedCategory.setName("월급");
    savedCategory.setType(CategoryType.INCOME);
    savedCategory.setMember(member);

    // stub 1
    when(memberService.validateAndGetMember(loginMember)).thenReturn(member);

    // stub 2
    when(categoryRepository.findById(any())).thenReturn(Optional.of(savedCategory));

    // stub 3
    when(categoryRepository.existsByNameAndTypeAndMember(any(), any(), any())).thenReturn(false);

    // stub 4
    when(categoryRepository.save(any())).thenReturn(savedCategory);

    // when
    CategoryResponseDto responseDto =
        categoryService.updateCategory(1L, requestDto, loginMember);

    // then
    assertEquals("적금", responseDto.getName());
    assertEquals("지출", responseDto.getType());
  }

  @Test
  void 카테고리_삭제_test() throws Exception {
    // given
    CategoryUpdateRequestDto requestDto = new CategoryUpdateRequestDto();
    requestDto.setName("적금");
    requestDto.setType(CategoryType.SPENDING);

    MemberEntity member = new MemberEntity();
    member.setId(1L);
    member.setRole(Authority.USER);
    member.setEmail("test@test.com");
    member.setPassword("1234");
    member.setName("홍길동");
    Member loginMember = Member.fromEntity(member);

    CategoryEntity savedCategory = new CategoryEntity();
    savedCategory.setId(1L);
    savedCategory.setName("월급");
    savedCategory.setType(CategoryType.INCOME);
    savedCategory.setMember(member);

    // stub 1
    when(memberService.validateAndGetMember(loginMember)).thenReturn(member);

    // stub 2
    when(categoryRepository.findById(any())).thenReturn(Optional.of(savedCategory));

    // when
    categoryService.deleteCategory(1L, loginMember);

    // then
    verify(categoryRepository).delete(savedCategory);
  }

  @Test
  void 카테고리_목록_test() throws Exception {
    // given
    CategoryUpdateRequestDto requestDto = new CategoryUpdateRequestDto();
    requestDto.setName("적금");
    requestDto.setType(CategoryType.SPENDING);

    MemberEntity member = new MemberEntity();
    member.setId(1L);
    member.setRole(Authority.USER);
    member.setEmail("test@test.com");
    member.setPassword("1234");
    member.setName("홍길동");
    Member loginMember = Member.fromEntity(member);

    List<CategoryEntity> categoryEntityList = new ArrayList<>();

    CategoryEntity category1 = new CategoryEntity();
    category1.setId(1L);
    category1.setName("월급");
    category1.setType(CategoryType.INCOME);
    category1.setMember(member);
    categoryEntityList.add(category1);

    CategoryEntity category2 = new CategoryEntity();
    category2.setId(2L);
    category2.setName("적금");
    category2.setType(CategoryType.SPENDING);
    category2.setMember(member);
    categoryEntityList.add(category2);

    // stub 1
    when(memberService.validateAndGetMember(loginMember)).thenReturn(member);

    // stub 2
    when(categoryRepository.findByMember(1L)).thenReturn(categoryEntityList);

    // when
    List<CategoryResponseDto> responseDtoList =
        categoryService.getAllCategories(Member.fromEntity(member));

    // then
    assertEquals(2, responseDtoList.size());
    assertEquals("월급", responseDtoList.get(0).getName());
    assertEquals("수입", responseDtoList.get(0).getType());
    assertEquals("적금", responseDtoList.get(1).getName());
    assertEquals("지출", responseDtoList.get(1).getType());
  }
}
