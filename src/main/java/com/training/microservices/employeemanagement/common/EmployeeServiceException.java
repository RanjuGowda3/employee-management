package com.training.microservices.employeemanagement.common;

public class EmployeeServiceException extends Exception{

	private static final long serialVersionUID = 6147502013955963959L;

	public EmployeeServiceException(final String message) {
		super(message);
	}
}
