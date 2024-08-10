package com.platform.selfcare.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.platform.selfcare.service.ConfigService;
import com.platform.selfcare.service.IUserService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
	
	@Autowired
	IUserService userService;
	
	@Autowired
	ConfigService configService;
	
	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		if (configService.getMailSenderActive()) {
			this.userService.sendConfirmationMailByUser(event.getAppUrl(), event.getLocale(), event.getUser());
		}
	}
}
