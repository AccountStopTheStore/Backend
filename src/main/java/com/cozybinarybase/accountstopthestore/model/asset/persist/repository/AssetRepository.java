package com.cozybinarybase.accountstopthestore.model.asset.persist.repository;

import com.cozybinarybase.accountstopthestore.model.asset.dto.constants.AssetType;
import com.cozybinarybase.accountstopthestore.model.asset.persist.entity.AssetEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<AssetEntity, Long> {

  boolean existsByNameAndTypeAndMember(String name, AssetType type, Long memberId);

  List<AssetEntity> findByMember(Long memberId);

  Optional<AssetEntity> findByNameAndMember(String name, Long memberId);

  void deleteAllByMember(Long id);
}
