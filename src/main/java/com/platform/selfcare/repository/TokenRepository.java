package com.platform.selfcare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.platform.selfcare.entity.User;
import com.platform.selfcare.entity.VerificationToken;
import com.platform.selfcare.enums.TokenType;

import jakarta.transaction.Transactional;

@Transactional
public interface TokenRepository extends JpaRepository<VerificationToken, Long> {
	
	Optional<VerificationToken> findByTokenAndType(String token, TokenType type);
	
	/**
	 * can not have two entries by OneToOne
	 */
	@Deprecated
	@Modifying
	@Query("UPDATE VerificationToken t SET t.expireDate = now() WHERE t.user = :user AND t.type = :type")
	void setAllExpiredByUserAndType(User user, TokenType type);
	
	Optional<VerificationToken> findByUserAndType(User user, TokenType type);
}
