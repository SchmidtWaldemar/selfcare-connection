package com.platform.selfcare.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.platform.selfcare.dto.RegisterUserDto;
import com.platform.selfcare.entity.Role;
import com.platform.selfcare.entity.User;
import com.platform.selfcare.entity.VerificationToken;
import com.platform.selfcare.enums.RoleType;
import com.platform.selfcare.enums.TokenStatus;
import com.platform.selfcare.enums.TokenType;
import com.platform.selfcare.exception.UserAlreadyExistsException;
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
	
	@Override
	public User registerNewUser(RegisterUserDto userDto) {
		
		Optional<User> exists = this.userRepository.findByEmail(userDto.getEmail());
		if (exists.isPresent()) {
			throw new UserAlreadyExistsException("User account already exists!");
		}
		
		BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
		String encodedPw = "{BCRYPT}" + pwEncoder.encode(userDto.getPassword());
		
		final User user = new User(userDto.getEmail(), encodedPw);
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		
		return this.userRepository.save(user);
	}

	@Override
	public void sendConfirmationMailByUser(String appUrl, Locale locale, User user) {
		final String token = UUID.randomUUID().toString();
		VerificationToken registerToken = new VerificationToken(token, user, TokenType.REGISTER_TOKEN);
		this.tokenRepository.setAllExpiredByUserAndType(user, TokenType.REGISTER_TOKEN);
		this.tokenRepository.save(registerToken);
		
		try {
			boolean sendStatus = this.sendService.sendRegistrationEmail(appUrl, locale, user, token);
			if (!sendStatus) {
				System.err.println("failed to send mail");
			}
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public VerificationToken validateVerificationToken(String token, TokenType type) {
		if (token == null || token.isBlank()) {
			return new VerificationToken(TokenStatus.TOKEN_NOT_EXIST);
		}
		
		final Optional<VerificationToken> verificatedToken = tokenRepository.findByTokenAndType(token, type);
		if (!verificatedToken.isPresent()) {
			return new VerificationToken(TokenStatus.TOKEN_INVALID);
		}
		
		final User user = verificatedToken.get().getUser();
		final Calendar cal = Calendar.getInstance();
		if ((verificatedToken.get().getExpireDate().getTime() - cal.getTime().getTime()) <= 0) {
			tokenRepository.delete(verificatedToken.get());
			verificatedToken.get().setStatus(TokenStatus.TOKEN_EXPIRED);
			return verificatedToken.get();
		}
		
		user.setEnabled(true);
		Optional<Role> uRole = this.roleRepository.findByName(RoleType.USER.getName());
		if (uRole.isPresent()) {
			Set<Role> roles = new HashSet<Role>();
			roles.add(uRole.get());
			user.setRoles(roles);
		}
		else {
			verificatedToken.get().setStatus(TokenStatus.ERROR);
			return verificatedToken.get();
		}
		
		tokenRepository.delete(verificatedToken.get());
		userRepository.save(user);
		
		verificatedToken.get().setStatus(TokenStatus.TOKEN_VALID);
		return verificatedToken.get();
	}
}
