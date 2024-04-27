package com.ecommerce.retail_electronicsapp.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.retail_electronicsapp.entity.RefreshToken;
import com.ecommerce.retail_electronicsapp.entity.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer>{

	boolean existsByTokenAndIsBlocked(String rt, boolean b);

	Optional<RefreshToken> findByToken(String refreshToken);

	boolean existsByToken(String refreshToken);

	List<RefreshToken> findAllByIsBlocked(boolean b);

	void deleteAllByIsBlockedTrue();

	Iterable<? extends RefreshToken> findAllByExpirationLessThan(LocalDateTime now);

}
