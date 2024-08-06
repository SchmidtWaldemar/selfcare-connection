package com.platform.selfcare.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
	
	private Pattern pattern;
	
	private Matcher matcher;
	
	private static final String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w-]+\\.)+[\\w]+[\\w]$";

	/**
	 * override to make sure validator will use your customized validation
	 */
	@Override
	public void initialize(final ValidEmail constraintAnnotation) {}
	
	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		return validateEmail(email);
	}
	
	private boolean validateEmail(final String email) {
		this.pattern = Pattern.compile(EMAIL_PATTERN);
		this.matcher = this.pattern.matcher(email);
		return matcher.matches();
	}
}
