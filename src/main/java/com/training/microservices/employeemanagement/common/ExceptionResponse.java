package com.training.microservices.employeemanagement.common;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.training.microservices.employeemanagement.model.Employee;

public class ExceptionResponse {
	
	private String message;
	private Integer statusCode;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer notFound) {
		this.statusCode = notFound;
	}

}
