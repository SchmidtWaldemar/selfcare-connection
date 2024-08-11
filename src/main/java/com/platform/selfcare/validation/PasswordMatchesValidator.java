package com.platform.selfcare.validation;

import org.springframework.beans.BeanWrapperImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
	
	private String password;
	private String matchingPassword;
	
	/**
	 * override to make sure validator will use your customized validation
	 */
	@Override
	public void initialize(final PasswordMatches constraintAnnotation) {
		this.setPassword(constraintAnnotation.password());
		this.setMatchingPassword(constraintAnnotation.matchingPassword());
	}
	
	@Override
	public boolean isValid(Object dto, ConstraintValidatorContext context) {
		Object passwordValue = new BeanWrapperImpl(dto).getPropertyValue(password);
		Object confirmPasswordValue = new BeanWrapperImpl(dto).getPropertyValue(matchingPassword);
		
		return (passwordValue != null) ? passwordValue.equals(confirmPasswordValue) : confirmPasswordValue == null;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String confirmPassword) {
		this.matchingPassword = confirmPassword;
	}
}
