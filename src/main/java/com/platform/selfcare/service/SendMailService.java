package com.platform.selfcare.service;

import java.io.IOException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.platform.selfcare.entity.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class SendMailService implements ISendMailService {

	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	ConfigService configService;
	
	@Override
	public boolean sendRegistrationEmail(String appUrl, Locale locale, User user, String token) throws MessagingException, IOException {
		final String confirmationUrl = appUrl + "/register/registrationConfirm?token=" + token;
		String message = "Die Registrierung war erfolgreich. Bitte klicken Sie auf den folgenden Link, um Ihre E-Mail zu bestätigen: \n" + confirmationUrl;
		return sentMailToUser(user.getEmail(), "Registrierung Bestätigung", message);
	}

	@Override
	public boolean sendPasswordForgotten(String appUrl, Locale locale, User user, String token) throws MessagingException, IOException {
		final String confirmationUrl = appUrl + "/changeForgottenPassword?token=" + token;
		String message = "Sie haben die Passwort Vergessen Funktion beantragt? Wenn ja, klicken Sie bitte auf den folgenden Link: \n" + confirmationUrl;
		return sentMailToUser(user.getEmail(), "Passwort Vergessen?", message);
	}

	
	private boolean sentMailToUser(final String recipientMail, final String subject, final String message) throws MessagingException {
		if (message == null || message.length() == 0) {
			return false;
		}
		
		MimeMessage msg = this.mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, false);
		helper.setFrom(this.configService.getSupportMail());
		helper.setTo(recipientMail);
		helper.setSubject(subject);
		helper.setText(message);
		
		try {
			mailSender.send(msg);
		} catch (MailException e) {
			System.err.println("ERROR by sending mail: " + e.getMessage());
			return false;
		}
		
		return true;
	}
}
