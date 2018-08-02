package com.training.microservices.employeemanagement.common;

public class ResourceNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -710011661770792018L;

	public ResourceNotFoundException(final String message) {
		super(message);
	}
}
