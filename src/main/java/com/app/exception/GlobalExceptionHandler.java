package com.app.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(WorldNotFoundException.class)
	public ResponseEntity<Void> handleWorldNotFoundException(WorldNotFoundException ex) {
		return ResponseEntity.notFound().build();
	}
}
