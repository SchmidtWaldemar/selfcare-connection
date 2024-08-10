package com.platform.selfcare.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * annotation interface to describe email format
 */
@Documented
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {

	/**
	 * custom output message
	 * 
	 * @return message as string
	 */
	String message() default "Fehlerhaftes Passwort";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
