package com.nt.exc;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InventoryNotFoundException.class)
	public ResponseEntity<?> handleInventoryNotFound(InventoryNotFoundException ex) {

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(Map.of("timestamp", LocalDateTime.now(), "message", ex.getMessage()));
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<?> handleIllegalState(IllegalStateException ex) {

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("timestamp", LocalDateTime.now(), "message", ex.getMessage()));
	}
}