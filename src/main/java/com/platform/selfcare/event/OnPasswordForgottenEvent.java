package com.platform.selfcare.event;

import java.util.Locale;

import com.platform.selfcare.entity.User;

@SuppressWarnings("serial")
public class OnPasswordForgottenEvent extends OnUserSendMailEvent {

	public OnPasswordForgottenEvent(User user, Locale locale, String appUrl) {
		super(user, locale, appUrl);
	}
}
