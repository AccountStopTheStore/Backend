package com.cozybinarybase.accountstopthestore.model.challenge.persist.repository;

import com.cozybinarybase.accountstopthestore.model.challenge.persist.entity.ChallengeGroupEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeGroupRepository extends JpaRepository<ChallengeGroupEntity, Long> {

  Optional<ChallengeGroupEntity> findByInviteLink(String inviteLink);

  void deleteAllByAdmin_Id(Long memberId);
}
