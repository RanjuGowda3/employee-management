package com.training.microservices.employeemanagement.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerControllerAdvice {

	/*
	 * @ExceptionHandler(Exception.class)
	 * 
	 * @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	 * public @ResponseBody ExceptionResponse handleException(final Exception
	 * exception, final HttpServletRequest request) { ExceptionResponse error =
	 * new ExceptionResponse(); error.setMessage(exception.getMessage()); return
	 * error; }
	 */

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception exception, final HttpServletRequest request,
			final HttpServletResponse response) {
		ExceptionResponse error = new ExceptionResponse();
		error.setMessage(exception.getMessage());
		error.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ExceptionResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/*
	 * @ExceptionHandler(ResourceNotFoundException.class)
	 * 
	 * @ResponseStatus(value = HttpStatus.NOT_FOUND) public @ResponseBody
	 * ExceptionResponse handleResourceNotFound(final ResourceNotFoundException
	 * exception, final HttpServletRequest request,final HttpServletResponse
	 * response) {
	 * 
	 * ExceptionResponse error = new ExceptionResponse();
	 * error.setMessage(exception.getMessage()); return error; }
	 */

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(Exception ex, final HttpServletRequest request,
			final HttpServletResponse response) {
		ExceptionResponse error = new ExceptionResponse();
		error.setMessage(ex.getMessage());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<ExceptionResponse>(error, HttpStatus.NOT_FOUND);
	}

	/*
	 * @ExceptionHandler(NullPointerException.class)
	 * 
	 * @ResponseStatus(value = HttpStatus.NO_CONTENT) public @ResponseBody
	 * ExceptionResponse handleResourceNotFound(final NullPointerException
	 * exception, final HttpServletRequest request,final HttpServletResponse
	 * response) { ExceptionResponse error = new ExceptionResponse();
	 * error.setMessage(exception.getMessage()); return error; }
	 */

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ExceptionResponse> handleResourceNotFound(NullPointerException exception) {
		ExceptionResponse error = new ExceptionResponse();
		error.setMessage(exception.getMessage());
		error.setStatusCode(HttpStatus.NO_CONTENT.value());
		return new ResponseEntity<ExceptionResponse>(error, HttpStatus.NO_CONTENT);
	}

}
