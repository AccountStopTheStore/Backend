package com.cozybinarybase.accountstopthestore.model.accountbook.service;

import com.cozybinarybase.accountstopthestore.common.service.AddressService;
import com.cozybinarybase.accountstopthestore.model.accountbook.domain.AccountBook;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.AccountBookImageResponseDto;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.AccountBookResponseDto;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.AccountBookSaveRequestDto;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.AccountBookSaveResponseDto;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.AccountBookUpdateRequestDto;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.AccountBookUpdateResponseDto;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.StatisticsData;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.TransactionStatisticsResponse;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.constants.TransactionType;
import com.cozybinarybase.accountstopthestore.model.accountbook.exception.AccountBookNotValidException;
import com.cozybinarybase.accountstopthestore.model.accountbook.persist.entity.AccountBookEntity;
import com.cozybinarybase.accountstopthestore.model.accountbook.persist.repository.AccountBookRepository;
import com.cozybinarybase.accountstopthestore.model.asset.exception.AssetNotValidException;
import com.cozybinarybase.accountstopthestore.model.asset.persist.entity.AssetEntity;
import com.cozybinarybase.accountstopthestore.model.asset.persist.repository.AssetRepository;
import com.cozybinarybase.accountstopthestore.model.category.exception.CategoryNotValidException;
import com.cozybinarybase.accountstopthestore.model.category.persist.entity.CategoryEntity;
import com.cozybinarybase.accountstopthestore.model.category.persist.repository.CategoryRepository;
import com.cozybinarybase.accountstopthestore.model.images.exception.ImageNotValidException;
import com.cozybinarybase.accountstopthestore.model.images.persist.entity.ImageEntity;
import com.cozybinarybase.accountstopthestore.model.images.persist.entity.ImageEntity.ImageType;
import com.cozybinarybase.accountstopthestore.model.images.persist.repository.ImageRepository;
import com.cozybinarybase.accountstopthestore.model.member.domain.Member;
import com.cozybinarybase.accountstopthestore.model.member.service.MemberService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AccountBookService {

  private final AccountBookRepository accountBookRepository;
  private final CategoryRepository categoryRepository;
  private final AssetRepository assetRepository;
  private final MemberService memberService;
  private final AccountBook accountBook;
  private final ImageRepository imageRepository;
  private final AddressService addressService;
  @Value("${app.domainUrl}")
  private String domainUrl;

  @Transactional
  public AccountBookSaveResponseDto saveAccountBook(
      AccountBookSaveRequestDto requestDto, Member member) {
    memberService.validateAndGetMember(member);

    CategoryEntity categoryEntity = categoryRepository.findByNameAndMember(
            requestDto.getCategoryName(), member.getId())
        .orElseThrow(CategoryNotValidException::new);

    AssetEntity assetEntity = assetRepository.findByNameAndMember(requestDto.getAssetName(),
        member.getId()).orElseThrow(
        AssetNotValidException::new);

    List<ImageEntity> images = requestDto.getImageIds().stream()
        .map(imageId -> imageRepository.findByIdAndImageTypeAndMember(imageId, ImageType.ORIGINAL,
                member.getId())
            .orElseThrow(() -> new ImageNotValidException()))
        .collect(Collectors.toList());

    AccountBookEntity accountBookEntity = accountBook.createAccountBook(
        requestDto, categoryEntity.getId(), assetEntity.getId(), member.getId(),
        categoryEntity.getName(), assetEntity.getName(), images).toEntity();

    AccountBookEntity finalAccountBookEntity = accountBookEntity;
    images.forEach(image -> image.setAccountBook(finalAccountBookEntity));

    accountBookEntity.setImages(images);

    Map<String, String> coordinates = addressService.getCoordinates(accountBookEntity.getAddress());

    if (!coordinates.isEmpty()) {
      double latitude = Double.parseDouble(coordinates.get("y"));
      double longitude = Double.parseDouble(coordinates.get("x"));

      accountBookEntity.setLatitude(latitude);
      accountBookEntity.setLongitude(longitude);
    } else {
      accountBookEntity.setLatitude(null);
      accountBookEntity.setLongitude(null);
    }

    // AccountBookEntity 저장
    accountBookEntity = accountBookRepository.save(accountBookEntity);

    // DTO 변환 및 반환
    return AccountBookSaveResponseDto.fromEntity(accountBookEntity);
  }

  @Transactional
  public AccountBookUpdateResponseDto updateAccountBook(Long accountId,
      AccountBookUpdateRequestDto requestDto, Member member) {
    memberService.validateAndGetMember(member);

    AccountBookEntity accountBookEntity = accountBookRepository.findByIdAndMember(accountId, member.getId())
        .orElseThrow(AccountBookNotValidException::new);

    CategoryEntity categoryEntity = categoryRepository.findByNameAndMember(
            requestDto.getCategoryName(), member.getId())
        .orElseThrow(CategoryNotValidException::new);

    AssetEntity assetEntity = assetRepository.findByNameAndMember(requestDto.getAssetName(),
        member.getId()).orElseThrow(
        AssetNotValidException::new);

    List<ImageEntity> images = requestDto.getImageIds().stream()
        .map(imageId -> imageRepository.findByIdAndImageTypeAndMember(imageId, ImageType.ORIGINAL,
                member.getId())
            .orElseThrow(ImageNotValidException::new))
        .collect(Collectors.toList());

    AccountBook accountBookDomain = AccountBook.fromEntity(accountBookEntity);
    accountBookDomain.updateAccountBook(requestDto, categoryEntity.getId(), assetEntity.getId(), images);

    images.forEach(image -> {
      image.setAccountBook(accountBookEntity);
      imageRepository.save(image);
    });

    accountBookEntity.setImages(images);

    Map<String, String> coordinates = addressService.getCoordinates(accountBookEntity.getAddress());

    if (!coordinates.isEmpty()) {
      double latitude = Double.parseDouble(coordinates.get("y"));
      double longitude = Double.parseDouble(coordinates.get("x"));

      accountBookEntity.setLatitude(latitude);
      accountBookEntity.setLongitude(longitude);
    } else {
      accountBookEntity.setLatitude(null);
      accountBookEntity.setLongitude(null);
    }

    AccountBookEntity updatedAccountBookEntity = accountBookRepository.save(accountBookEntity);

    return AccountBookUpdateResponseDto.fromEntity(updatedAccountBookEntity);
  }

  @Transactional
  public void deleteAccountBook(Long accountId, Member member) {
    memberService.validateAndGetMember(member);

    AccountBookEntity accountBookEntity = accountBookRepository.findByIdAndMember(accountId,
            member.getId())
        .orElseThrow(AccountBookNotValidException::new);

    accountBookRepository.delete(accountBookEntity);
  }

  @Transactional(readOnly = true)
  public List<AccountBookResponseDto> getAccountBooks(LocalDate startDate, LocalDate endDate,
      TransactionType transactionType, int page, int limit, Member member) {
    memberService.validateAndGetMember(member);

    if (startDate.isAfter(endDate) || endDate.isBefore(startDate)) {
      throw new AccountBookNotValidException("날짜 설정을 다시 해주시길 바랍니다.");
    }

    Pageable pageable = PageRequest.of(page, limit);

    List<AccountBookEntity> accountBookEntities =
        accountBookRepository.findByTransactedAtBetweenAndTransactionTypeAndMember(
            startDate.atStartOfDay(),
            endDate.plusDays(1).atStartOfDay(),
            transactionType,
            member.getId(),
            pageable).getContent();

    return AccountBookResponseDto.fromEntities(accountBookEntities);
  }

  @Transactional(readOnly = true)
  public List<AccountBookResponseDto> getMonthlyAccountBooks(
      YearMonth yearMonth, Member member) {

    memberService.validateAndGetMember(member);

    LocalDate startDate = yearMonth.atDay(1);
    LocalDate endDate = yearMonth.atEndOfMonth();

    List<AccountBookEntity> accountBookEntities =
        accountBookRepository.findByTransactedAtBetweenAndMember(
            startDate.atStartOfDay(),
            endDate.atTime(LocalTime.MAX),
            member.getId());

    return AccountBookResponseDto.fromEntities(accountBookEntities);
  }

  @Transactional(readOnly = true)
  public List<String> getCategoryNamesByKeyword(String query, int limit,
      Member member) {
    memberService.validateAndGetMember(member);

    List<String> categories = categoryRepository.findByMemberAndNameStartingWithIgnoreCase(
            member.getId(), query)
        .stream()
        .map(CategoryEntity::getName)
        .collect(Collectors.toList());

    return categories;
  }

  @Transactional(readOnly = true)
  public List<AccountBookResponseDto> search(String keyword, LocalDate startDate, LocalDate endDate,
      String categoryName,
      Long minPrice, Long maxPrice, int page, int limit, Member member) {

    if (startDate.isAfter(endDate) || endDate.isBefore(startDate)) {
      throw new AccountBookNotValidException("날짜 설정을 다시 해주시길 바랍니다.");
    }

    Pageable pageable = PageRequest.of(page, limit);

    List<AccountBookEntity> accountBookEntities =
        accountBookRepository.findByMemoContainingAndTransactedAtBetweenAndCategory_NameAndAmountBetweenAndMember(
            keyword, startDate.atStartOfDay(), endDate.atStartOfDay(), categoryName, minPrice,
            maxPrice, member.getId(), pageable).getContent();

    return AccountBookResponseDto.fromEntities(accountBookEntities);
  }

  @Transactional(readOnly = true)
  public List<AccountBookImageResponseDto> getAccountBookImages(Long accountId, Member member) {
    memberService.validateAndGetMember(member);

    AccountBookEntity accountBookEntity = accountBookRepository.findByIdAndMember(accountId,
            member.getId())
        .orElseThrow(AccountBookNotValidException::new);

    List<ImageEntity> originalImages = accountBookEntity.getImages().stream()
        .filter(image -> image.getImageType() == ImageEntity.ImageType.ORIGINAL)
        .collect(Collectors.toList());

    return originalImages.stream()
        .map(originalImage -> {
          String compressedImageUrl = imageRepository.findByOriginalImageAndImageType(
                  originalImage.getId(), ImageEntity.ImageType.COMPRESSED)
              .map(img -> domainUrl + "/images/" + img.getImageFileName())
              .orElse(null);

          String thumbnailUrl = imageRepository.findByOriginalImageAndImageType(
                  originalImage.getId(), ImageEntity.ImageType.THUMBNAIL)
              .map(img -> domainUrl + "/images/" + img.getImageFileName())
              .orElse(null);

          return new AccountBookImageResponseDto(originalImage.getId(), compressedImageUrl, thumbnailUrl);
        })
        .collect(Collectors.toList());
  }

  public TransactionStatisticsResponse getTransactionStatistics(LocalDate startDate,
      LocalDate endDate, TransactionType transactionType, Member member) {
    memberService.validateAndGetMember(member);

    List<StatisticsData> statistics = accountBookRepository.findTransactionStatistics(startDate,
        endDate, transactionType, member.getId());
    return TransactionStatisticsResponse.builder()
        .startDate(startDate)
        .endDate(endDate)
        .transactionType(transactionType)
        .statisticsData(statistics)
        .build();
  }

  public AccountBookResponseDto getAccountBook(Long accountId, Member member) {
    memberService.validateAndGetMember(member);

    AccountBookEntity accountBookEntity = accountBookRepository.findByIdAndMember(accountId,
            member.getId())
        .orElseThrow(AccountBookNotValidException::new);

    return AccountBookResponseDto.fromEntity(accountBookEntity);
  }

  public List<AccountBookResponseDto> findAccountBooksNearby(double latitude, double longitude, double radius,
      Member member) {
    List<AccountBookEntity> accountBookEntities = accountBookRepository
        .findWithinRadius(latitude, longitude, radius, member.getId());
    return AccountBookResponseDto.fromEntities(accountBookEntities);
  }
}
