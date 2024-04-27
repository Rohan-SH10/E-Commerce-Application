package com.ecommerce.retail_electronicsapp.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.retail_electronicsapp.entity.AccessToken;
import com.ecommerce.retail_electronicsapp.entity.User;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Integer>{

	String findByIsBlockedAndToken(boolean isBlocked, String token);

	boolean existsByTokenAndIsBlocked(String at, boolean b);

	Optional<AccessToken> findByToken(String accessToken);

	boolean existsByToken(String accessToken);

	List<AccessToken> findAllByIsBlocked(boolean b);

	void deleteAllByIsBlockedTrue();

	Iterable<? extends AccessToken> findAllByExpirationLessThan(LocalDateTime localDateTime);

}
