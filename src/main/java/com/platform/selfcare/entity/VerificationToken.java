package com.platform.selfcare.entity;

import java.util.Calendar;
import java.util.Date;

import com.platform.selfcare.enums.TokenStatus;
import com.platform.selfcare.enums.TokenType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * token for confirmation of 2F authentications
 */
@Entity
@Table(name="token")
public class VerificationToken {
	
	private static final int EXPIRATION = 60 * 24;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String token;
	
	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumns({
		@JoinColumn(name = "user_id", referencedColumnName = "id")
	})
	private User user;
	
	private Date expireDate;
	
	private TokenType type;
	
	@Transient
	private TokenStatus status;
	
	public VerificationToken() {}
	
	public VerificationToken(final TokenStatus status) {
		this.status = status;
	}
	
	public VerificationToken(final String token, final TokenType type) {
		this.token = token;
		this.type = type;
		this.expireDate = calculateExpireDate(EXPIRATION);
	}

	public VerificationToken(final String token, final User user, final TokenType type) {
		this.token = token;
		this.user = user;
		this.type = type;
		this.expireDate = calculateExpireDate(EXPIRATION);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public TokenType getType() {
		return type;
	}

	public void setType(TokenType type) {
		this.type = type;
	}

	public TokenStatus getStatus() {
		return status;
	}

	public void setStatus(TokenStatus status) {
		this.status = status;
	}
	
	public boolean isExpired() {
		final Calendar cal = Calendar.getInstance();
		return this.expireDate.before(cal.getTime());
	}

	private Date calculateExpireDate(int expiration) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(new Date().getTime());
		cal.add(Calendar.MINUTE, expiration);
		return new Date(cal.getTime().getTime());
	}
}
