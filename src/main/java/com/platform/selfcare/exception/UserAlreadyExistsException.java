package com.platform.selfcare.exception;

public class UserAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 2258797152368208731L;

	public UserAlreadyExistsException() {
		super();
	}
	
	public UserAlreadyExistsException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	public UserAlreadyExistsException(final String message) {
		super(message);
	}
	
	public UserAlreadyExistsException(final Throwable cause) {
		super(cause);
	}
}
