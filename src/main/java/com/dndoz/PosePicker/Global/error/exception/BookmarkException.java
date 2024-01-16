package com.dndoz.PosePicker.Global.error.exception;

public class BookmarkException extends RuntimeException{
	private ErrorCode errorCode;

	public BookmarkException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public BookmarkException(String message) {
		super(message);
	}
}
