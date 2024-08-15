package com.platform.selfcare.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.platform.selfcare.dto.RegisterUserDto;
import com.platform.selfcare.entity.Candidate;
import com.platform.selfcare.entity.Role;
import com.platform.selfcare.entity.User;
import com.platform.selfcare.entity.VerificationToken;
import com.platform.selfcare.enums.RoleType;
import com.platform.selfcare.enums.TokenStatus;
import com.platform.selfcare.enums.TokenType;
import com.platform.selfcare.exception.UserAlreadyExistsException;
import com.platform.selfcare.repository.CandidateRepository;
import com.platform.selfcare.repository.RoleRepository;
import com.platform.selfcare.repository.TokenRepository;
import com.platform.selfcare.repository.UserRepository;

import jakarta.mail.MessagingException;

@Service
public class UserService implements IUserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TokenRepository tokenRepository;
	
	@Autowired
	ISendMailService sendService;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	CandidateRepository candidateRepository;
	
	@Override
	public User registerNewUser(RegisterUserDto userDto) {
		
		Optional<User> exists = this.userRepository.findByEmail(userDto.getEmail());
		if (exists.isPresent()) {
			throw new UserAlreadyExistsException("User account already exists!");
		}
		
		String encodedPw = getEncryptedPassword(userDto.getPassword());
		
		final User user = new User(userDto.getEmail(), encodedPw);
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		
		return this.userRepository.save(user);
	}

	private String getEncryptedPassword(final String password) {
		BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
		return "{BCRYPT}" + pwEncoder.encode(password);
	}

	@Override
	public void sendConfirmationMailByUserAndType(String appUrl, Locale locale, User user, TokenType type) {
		Optional<VerificationToken> existingToken = this.tokenRepository.findByUserAndType(user, type);
		if (existingToken.isPresent()) {
			if (existingToken.get().isExpired()) {
				tokenRepository.delete(existingToken.get());
			}
			else {
				// do nothing to prevent hacker sending SPAM to registered user
				return;
			}
		}
		
		final String token = UUID.randomUUID().toString();
		VerificationToken registerToken = new VerificationToken(token, user, type);
		//this.tokenRepository.setAllExpiredByUserAndType(user, type);
		this.tokenRepository.save(registerToken);
		
		try {
			boolean sendStatus = false;
			if (type.equals(TokenType.REGISTER_TOKEN)) {
				sendStatus = this.sendService.sendRegistrationEmail(appUrl, locale, user, token);
			}
			else if (type.equals(TokenType.PASSWORD_TOKEN)) {
				sendStatus = this.sendService.sendPasswordForgotten(appUrl, locale, user, token);
			}
			
			if (!sendStatus) {
				System.err.println("failed to send mail");
			}
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public VerificationToken validateVerificationToken(String token, TokenType type, boolean devaluate) {
		if (token == null || token.isBlank()) {
			return new VerificationToken(TokenStatus.TOKEN_NOT_EXIST);
		}
		
		final Optional<VerificationToken> verificatedToken = tokenRepository.findByTokenAndType(token, type);
		if (!verificatedToken.isPresent()) {
			return new VerificationToken(TokenStatus.TOKEN_INVALID);
		}
		
		final User user = verificatedToken.get().getUser();
		
		if (verificatedToken.get().isExpired()) {
			tokenRepository.delete(verificatedToken.get());
			verificatedToken.get().setStatus(TokenStatus.TOKEN_EXPIRED);
			return verificatedToken.get();
		}
		
		boolean success = false;
		if (type.equals(TokenType.REGISTER_TOKEN)) {
			setUserEnableStatus(user, true);
			success = true;
		}
		else if (type.equals(TokenType.PASSWORD_TOKEN)) {
			success = true;
		}
		
		if (!success) {
			verificatedToken.get().setStatus(TokenStatus.ERROR);
			return verificatedToken.get();
		}
		
		if (devaluate) {
			tokenRepository.delete(verificatedToken.get());
		}
		
		verificatedToken.get().setStatus(TokenStatus.TOKEN_VALID);
		return verificatedToken.get();
	}

	@Override
	public void setUserEnableStatus(final User user, boolean enableStatus) {
		user.setEnabled(enableStatus);
		Optional<Role> uRole = this.roleRepository.findByName(RoleType.USER.getName());
		if (uRole.isPresent() && !user.hasRole(RoleType.USER.getName())) {
			Set<Role> roles = new HashSet<Role>();
			roles.add(uRole.get());
			user.setRoles(roles);
		}
		userRepository.save(user);
	}

	@Override
	public Optional<User> findUserByEmail(String email) {
		return this.userRepository.findByEmail(email);
	}

	@Override
	public void changePassword(User user, final String newPassword) {
		user.setPassword(getEncryptedPassword(newPassword));
		this.userRepository.save(user);
	}

	@Override
	public boolean checkPasswordMatched(User user, String oldPassword) {
		if (oldPassword == null || oldPassword.isBlank()) return false;
		
		BCryptPasswordEncoder crypt = new BCryptPasswordEncoder();
		return crypt.matches(oldPassword, user.getPassword().replaceFirst("\\{BCRYPT\\}", ""));
	}

	@Override
	public Optional<VerificationToken> findTokenByUser(User user, TokenType type) {
		return this.tokenRepository.findByUserAndType(user, type);
	}

	@Override
	public List<User> findAllUserByRole(RoleType role) {
		List<User> allUser = this.userRepository.findAll();
		List<User> result = null;
		if (allUser != null && allUser.size() > 0) {
			if (role.equals(RoleType.USER)) {
				// filter users with more privileges
				// change filter after insert more roles
				result = allUser.stream().filter(u -> !u.hasRole(RoleType.ADMIN.getName())).toList();
			}
			else if (role.equals(RoleType.ADMIN)) {
				result = allUser.stream().filter(u -> u.hasRole(RoleType.ADMIN.getName())).toList();
			}
		}
		
		return result;
	}

	@Override
	public List<Candidate> getCandidatures(User user) {
		return this.candidateRepository.findCandidatures(user);
	}
}
