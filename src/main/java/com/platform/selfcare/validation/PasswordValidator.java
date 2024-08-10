package com.platform.selfcare.validation;

import java.util.Arrays;

import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.RuleResult;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * describe here how passwords should be
 */
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
	
	protected static final int MIN_LENGTH = 8;
	protected static final int MAX_LENGTH = 30;
	
	@Override
	public void initialize(final ValidPassword constraintAnnotation) {}
	
	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		
		final org.passay.PasswordValidator validator = new org.passay.PasswordValidator(Arrays.asList(
			new LengthRule(MIN_LENGTH, MAX_LENGTH)	
		));
		
		final RuleResult result = validator.validate(new PasswordData(password));
		if (result.isValid()) {
			return true;
		}
		
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate("Passwort hat nicht die korrekte Länge").addConstraintViolation();
		return false;
	}
}
