package com.dndoz.PosePicker.Global.error.exception;

public class CustomTokenException extends RuntimeException {
	private ErrorCode errorCode;

	public CustomTokenException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public CustomTokenException(String message) {
		super(message);
	}
}
