package com.cozybinarybase.accountstopthestore.model.message.persist.repository;

import com.cozybinarybase.accountstopthestore.model.challenge.domain.ChallengeGroup;
import com.cozybinarybase.accountstopthestore.model.challenge.persist.entity.ChallengeGroupEntity;
import com.cozybinarybase.accountstopthestore.model.message.persist.entity.MessageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

  List<MessageEntity> findByGroup(ChallengeGroupEntity challengeGroupEntity);

  void deleteAllBySender_Id(Long memberId);

  void deleteAllByGroup(ChallengeGroupEntity challengeGroupEntity);
}
