package dev.jcasaslopez.classroom.exception;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dev.jcasaslopez.classroom.dto.StandardResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(FailedAuthenticatedException.class)
	public ResponseEntity<StandardResponse> handleFailedAuthenticatedException(FailedAuthenticatedException ex){
		StandardResponse response = new StandardResponse (LocalDateTime.now(), 
				ex.getMessage() , null, HttpStatus.UNAUTHORIZED);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}
	
	@ExceptionHandler(NoSuchClassroomException.class)
	public ResponseEntity<StandardResponse> handleNoSuchClassroomExceptionException(NoSuchClassroomException ex){
		StandardResponse response = new StandardResponse (LocalDateTime.now(), 
				ex.getMessage() , null, HttpStatus.NOT_FOUND);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	
	@ExceptionHandler(UserNoAdminException.class)
	public ResponseEntity<StandardResponse> handleUserNoAdminExceptionException(UserNoAdminException ex){
		StandardResponse response = new StandardResponse (LocalDateTime.now(), 
				ex.getMessage() , null, HttpStatus.FORBIDDEN);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardResponse> handleValidationException(MethodArgumentNotValidException ex){
		StandardResponse response = new StandardResponse (LocalDateTime.now(), 
				"Missing or invalid fields" , null, HttpStatus.BAD_REQUEST);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<StandardResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
		StandardResponse response = new StandardResponse (LocalDateTime.now(), 
				"The database already contains a classroom with this name" , null, HttpStatus.BAD_REQUEST);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

}
