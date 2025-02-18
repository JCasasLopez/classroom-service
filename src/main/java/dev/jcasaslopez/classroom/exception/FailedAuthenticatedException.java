package dev.jcasaslopez.classroom.exception;

public class FailedAuthenticatedException extends RuntimeException {
	public FailedAuthenticatedException(String message) {
        super(message);
    }
}
