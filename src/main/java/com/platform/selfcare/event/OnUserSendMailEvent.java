package com.platform.selfcare.event;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.platform.selfcare.entity.User;

@SuppressWarnings("serial")
abstract class OnUserSendMailEvent extends ApplicationEvent {

	private final String appUrl;
	private final Locale locale;
	private final User user;
	
	public OnUserSendMailEvent(final User user, final Locale locale, final String appUrl) {
		super(user);
		this.user = user;
		this.locale = locale;
		this.appUrl = appUrl;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public Locale getLocale() {
		return locale;
	}

	public User getUser() {
		return user;
	}
}
