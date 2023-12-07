package com.cozybinarybase.accountstopthestore.model.challenge.persist.repository;

import com.cozybinarybase.accountstopthestore.model.challenge.persist.entity.ChallengeGroupEntity;
import com.cozybinarybase.accountstopthestore.model.challenge.persist.entity.MemberGroupEntity;
import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberGroupRepository extends JpaRepository<MemberGroupEntity, Long> {

  List<MemberGroupEntity> findByChallengeGroup(ChallengeGroupEntity challengeGroupEntity);

  boolean existsByMemberAndChallengeGroup(MemberEntity memberEntity, ChallengeGroupEntity challengeGroupEntity);

  Optional<MemberGroupEntity> findByMemberAndChallengeGroup(MemberEntity memberEntity,
      ChallengeGroupEntity challengeGroupEntity);

  List<MemberGroupEntity> findByMember(MemberEntity entity);

  Long countByChallengeGroup(ChallengeGroupEntity entity);

  void deleteAllByMemberId(Long memberId);

  void deleteByChallengeGroup(ChallengeGroupEntity challengeGroupEntity);
}
