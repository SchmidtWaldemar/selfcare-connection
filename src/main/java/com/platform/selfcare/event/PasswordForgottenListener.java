package com.platform.selfcare.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.platform.selfcare.enums.TokenType;
import com.platform.selfcare.service.ConfigService;
import com.platform.selfcare.service.IUserService;

@Component
public class PasswordForgottenListener implements ApplicationListener<OnPasswordForgottenEvent> {
	
	@Autowired
	IUserService userService;
	
	@Autowired
	ConfigService configService;
	
	@Override
	public void onApplicationEvent(OnPasswordForgottenEvent event) {
		if (configService.getMailSenderActive()) {
			this.userService.sendConfirmationMailByUserAndType(event.getAppUrl(), event.getLocale(), event.getUser(), TokenType.PASSWORD_TOKEN);
		}
	}
}
