package com.test.backendapi.exception.handler;

public class CustomException extends RuntimeException{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4424522835517717369L;
	
	private String message;
	private int code;

	public CustomException(String message, int code) {
		super(message);
		this.code =code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
