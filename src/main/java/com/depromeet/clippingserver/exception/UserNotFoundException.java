package com.depromeet.clippingserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException() {
		super("UserNotFoundException");
	}
}
