package com.example.studyblog.repository;

import com.example.studyblog.domain.Member;
import com.example.studyblog.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByMember(Member member);
}
