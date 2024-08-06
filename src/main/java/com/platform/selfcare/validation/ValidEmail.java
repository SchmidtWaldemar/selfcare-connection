package com.platform.selfcare.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * annotation interface to describe email format
 */
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface ValidEmail {

	/**
	 * custom output message
	 * 
	 * @return message as string
	 */
	String message() default "Fehlerhafte E-Mail";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
