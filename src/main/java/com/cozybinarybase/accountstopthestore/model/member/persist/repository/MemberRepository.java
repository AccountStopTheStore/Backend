package com.cozybinarybase.accountstopthestore.model.member.persist.repository;

import com.cozybinarybase.accountstopthestore.model.member.persist.entity.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

  Optional<MemberEntity> findByEmail(String email);

  Optional<MemberEntity> findByRefreshToken(String refreshToken);

  Optional<MemberEntity> findMemberByEmail(String email);
}
