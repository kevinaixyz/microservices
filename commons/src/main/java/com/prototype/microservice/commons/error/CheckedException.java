package com.prototype.microservice.commons.error;

public class CheckedException extends Exception {

	private static final long serialVersionUID = 6507463868397779870L;

	private Object[] args;

	public CheckedException(String message) {
		super(message);
	}

	public CheckedException(String message, Object[] args) {
		super(message);
		this.args = args;
	}

	public Object[] getArgs() {
		return args;
	}

}
