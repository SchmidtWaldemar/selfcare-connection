package com.platform.selfcare.event;

import java.util.Locale;

import com.platform.selfcare.entity.User;

@SuppressWarnings("serial")
public class OnRegistrationCompleteEvent extends OnUserSendMailEvent {
	
	public OnRegistrationCompleteEvent(final User user, final Locale locale, final String appUrl) {
		super(user, locale, appUrl);
	}
}
