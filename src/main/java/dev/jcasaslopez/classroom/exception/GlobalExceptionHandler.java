package dev.jcasaslopez.classroom.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dev.jcasaslopez.classroom.shared.utility.StandardResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	
	@ExceptionHandler(NoSuchClassroomException.class)
	public ResponseEntity<StandardResponse<Void>> handleNoSuchClassroomExceptionException(NoSuchClassroomException ex){
		StandardResponse<Void> response = new StandardResponse<> (ex.getMessage(), null, HttpStatus.NOT_FOUND);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	
	@ExceptionHandler(UserNoAdminException.class)
	public ResponseEntity<StandardResponse<Void>> handleUserNoAdminExceptionException(UserNoAdminException ex){
		StandardResponse<Void> response = new StandardResponse<> (ex.getMessage() , null, HttpStatus.FORBIDDEN);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardResponse<Void>> handleValidationException(MethodArgumentNotValidException ex){
		StandardResponse<Void> response = new StandardResponse<> ("Missing or invalid fields" , null, HttpStatus.BAD_REQUEST);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<StandardResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
		StandardResponse<Void> response = new StandardResponse<> ("The database already contains a classroom with this name" , null, HttpStatus.CONFLICT);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

}
