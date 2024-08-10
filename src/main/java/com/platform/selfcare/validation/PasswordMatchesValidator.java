package com.platform.selfcare.validation;

import com.platform.selfcare.dto.RegisterUserDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
	
	/**
	 * override to make sure validator will use your customized validation
	 */
	@Override
	public void initialize(final PasswordMatches constraintAnnotation) {}
	
	@Override
	public boolean isValid(Object dto, ConstraintValidatorContext context) {
		if (dto instanceof RegisterUserDto) {
			final RegisterUserDto userDto = (RegisterUserDto) dto;
			if (userDto.getPassword().equals(userDto.getMatchingPassword())) {
				return true;
			}
		}
		
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate("Passwörter stimmen nicht überein").addConstraintViolation();
		return false;
	}
}
