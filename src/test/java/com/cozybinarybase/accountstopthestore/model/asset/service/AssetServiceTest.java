package com.cozybinarybase.accountstopthestore.model.asset.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cozybinarybase.accountstopthestore.model.asset.domain.Asset;
import com.cozybinarybase.accountstopthestore.model.asset.dto.AssetResponseDto;
import com.cozybinarybase.accountstopthestore.model.asset.dto.AssetSaveRequestDto;
import com.cozybinarybase.accountstopthestore.model.asset.dto.AssetUpdateRequestDto;
import com.cozybinarybase.accountstopthestore.model.asset.dto.constants.AssetType;
import com.cozybinarybase.accountstopthestore.model.asset.persist.entity.AssetEntity;
import com.cozybinarybase.accountstopthestore.model.asset.persist.repository.AssetRepository;
import com.cozybinarybase.accountstopthestore.model.member.domain.Member;
import com.cozybinarybase.accountstopthestore.model.member.dto.constants.Authority;
import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
import com.cozybinarybase.accountstopthestore.model.member.service.MemberService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Disabled
class AssetServiceTest {

  @InjectMocks
  private AssetService assetService;

  @Mock
  private MemberService memberService;

  @Mock
  private AssetRepository assetRepository;

  @Mock
  private Asset assetMock;

  @Test
  void 자산_추가_test() throws Exception {
    // given
    AssetSaveRequestDto requestDto = new AssetSaveRequestDto();
    requestDto.setAssetType(AssetType.CARD);
    requestDto.setAssetName("우리카드");
    requestDto.setAmount(100000L);
    requestDto.setStatementDay(10);
    requestDto.setDueDay(20);
    requestDto.setMemo("메모");

    MemberEntity member = new MemberEntity();
    member.setId(1L);
    member.setRole(Authority.USER);
    member.setEmail("test@test.com");
    member.setPassword("1234");
    member.setName("홍길동");
    Member loginMember = Member.fromEntity(member);

    AssetEntity savedAsset = AssetEntity.builder()
        .id(1L)
        .type(AssetType.CARD)
        .name("우리카드")
        .amount(100000L)
        .statementDay(10)
        .dueDay(20)
        .memo("메모")
        .build();

    Asset assetDomain = Asset.builder()
        .type(AssetType.CARD)
        .name("우리카드")
        .amount(100000L)
        .statementDay(10)
        .dueDay(20)
        .memo("메모")
        .build();

    // stub 1
    when(memberService.validateAndGetMember(loginMember)).thenReturn(member);

    // stub 2
    when(assetRepository.existsByNameAndTypeAndMember(any(), any(), any())).thenReturn(false);

    // stub 3
    when(assetMock.createAsset(requestDto, 1L)).thenReturn(assetDomain);

    // stub 4
    when(assetRepository.save(any())).thenReturn(savedAsset);

    // when
    AssetResponseDto responseDto = assetService.saveAsset(requestDto, loginMember);

    // then
    assertEquals("카드", responseDto.getAssetType());
    assertEquals("우리카드", requestDto.getAssetName());
    assertEquals(100000L, requestDto.getAmount());
    assertEquals(10, requestDto.getStatementDay());
    assertEquals(20, requestDto.getDueDay());
    assertEquals("메모", requestDto.getMemo());
  }

  @Test
  void 자산_수정_test() throws Exception {
    // given
    AssetUpdateRequestDto requestDto = new AssetUpdateRequestDto();
    requestDto.setAssetType(AssetType.CARD);
    requestDto.setAssetName("우리카드");
    requestDto.setAmount(100000L);
    requestDto.setStatementDay(10);
    requestDto.setDueDay(20);
    requestDto.setMemo("메모 수정");
//    requestDto.setCreatedAt(LocalDateTime.of(2023, 11, 7, 11, 0, 0));
//    requestDto.setUpdatedAt(LocalDateTime.of(2023, 11, 7, 12, 0, 0));

    MemberEntity member = new MemberEntity();
    member.setId(1L);
    member.setRole(Authority.USER);
    member.setEmail("test@test.com");
    member.setPassword("1234");
    member.setName("홍길동");
    Member loginMember = Member.fromEntity(member);

    AssetEntity savedAsset = AssetEntity.builder()
        .id(1L)
        .type(AssetType.MONEY)
        .name("월급")
        .amount(200000L)
        .statementDay(1)
        .dueDay(2)
        .memo("메모")
        .member(member)
        .build();

    // stub 1
    when(memberService.validateAndGetMember(loginMember)).thenReturn(member);

    // stub 2
    when(assetRepository.findById(any())).thenReturn(Optional.of(savedAsset));

    // stub 3
    when(assetRepository.existsByNameAndTypeAndMember(any(), any(), any())).thenReturn(false);

    // stub 4
    when(assetRepository.save(any())).thenReturn(savedAsset);

    // when
    AssetResponseDto responseDto = assetService.updateAsset(1L, requestDto, loginMember);

    // then
    assertEquals("카드", responseDto.getAssetType());
    assertEquals("우리카드", requestDto.getAssetName());
    assertEquals(100000L, requestDto.getAmount());
    assertEquals(10, requestDto.getStatementDay());
    assertEquals(20, requestDto.getDueDay());
    assertEquals("메모 수정", requestDto.getMemo());
    assertEquals("2023-11-07T11:00", responseDto.getCreatedAt().toString());
    assertEquals("2023-11-07T12:00", responseDto.getUpdatedAt().toString());
  }

  @Test
  void 자산_삭제_test() throws Exception {
    // given
    MemberEntity member = new MemberEntity();
    member.setId(1L);
    member.setRole(Authority.USER);
    member.setEmail("test@test.com");
    member.setPassword("1234");
    member.setName("홍길동");
    Member loginMember = Member.fromEntity(member);

    AssetEntity savedAsset = AssetEntity.builder()
        .id(1L)
        .type(AssetType.MONEY)
        .name("월급")
        .amount(200000L)
        .statementDay(1)
        .dueDay(2)
        .memo("메모")
        .member(member)
        .build();

    // stub 1
    when(memberService.validateAndGetMember(loginMember)).thenReturn(member);

    // stub 2
    when(assetRepository.findById(any())).thenReturn(Optional.of(savedAsset));

    // when
    assetService.deleteAsset(1L, loginMember);

    // then
    verify(assetRepository).delete(savedAsset);
  }

  @Test
  void 자산_목록_test() throws Exception {
    // given
    MemberEntity member = new MemberEntity();
    member.setId(1L);
    member.setRole(Authority.USER);
    member.setEmail("test@test.com");
    member.setPassword("1234");
    member.setName("홍길동");
    Member loginMember = Member.fromEntity(member);

    List<AssetEntity> assetEntityList = new ArrayList<>();

    assetEntityList.add(AssetEntity.builder()
        .id(1L)
        .type(AssetType.CARD)
        .name("우리카드")
        .amount(100000L)
        .statementDay(10)
        .dueDay(20)
        .memo("메모")
        .build());

    assetEntityList.add(AssetEntity.builder()
        .id(1L)
        .type(AssetType.MONEY)
        .name("적금")
        .amount(200000L)
        .statementDay(1)
        .dueDay(2)
        .memo("메모2")
        .build());

    // stub 1
    when(memberService.validateAndGetMember(loginMember)).thenReturn(member);

    // stub 2
    when(assetRepository.findByMember(1L)).thenReturn(assetEntityList);

    // when
    List<AssetResponseDto> responseDtoList = assetService.getAllAssets(loginMember);

    // then
    assertEquals(2, responseDtoList.size());
    assertEquals("카드", responseDtoList.get(0).getAssetType());
    assertEquals("우리카드", responseDtoList.get(0).getAssetName());
    assertEquals(100000L, responseDtoList.get(0).getAmount());
    assertEquals(10, responseDtoList.get(0).getStatementDay());
    assertEquals(20, responseDtoList.get(0).getDueDay());
    assertEquals("메모", responseDtoList.get(0).getMemo());

    assertEquals("현금", responseDtoList.get(1).getAssetType());
    assertEquals("적금", responseDtoList.get(1).getAssetName());
    assertEquals(200000L, responseDtoList.get(1).getAmount());
    assertEquals(1, responseDtoList.get(1).getStatementDay());
    assertEquals(2, responseDtoList.get(1).getDueDay());
    assertEquals("메모2", responseDtoList.get(1).getMemo());
  }
}