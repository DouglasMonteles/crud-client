package com.devsuperior.crudclients.resources.exception;

import java.time.Instant;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.crudclients.services.exceptions.DatabaseException;
import com.devsuperior.crudclients.services.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(value = {
			ResourceNotFoundException.class, 
			EntityNotFoundException.class,
	})
	public ResponseEntity<StandardError> entityNotFoundException(Exception e,
			HttpServletRequest request) {
		var status = HttpStatus.NOT_FOUND;
		var error = new StandardError();
		
		error.setTimestamp(Instant.now());
		error.setStatus(status.value());
		error.setError("Resource not found");
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		
		return ResponseEntity.status(status).body(error);
	}
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> databaseException(DatabaseException e, 
			HttpServletRequest request) {
		var status = HttpStatus.BAD_REQUEST;
		var error = new StandardError();
		
		error.setTimestamp(Instant.now());
		error.setStatus(status.value());
		error.setError("Database Exception");
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		
		return ResponseEntity.status(status).body(error);
	}
	
}
