package dev.jcasaslopez.classroom.exception;

public class UserNoAdminException extends RuntimeException {
	public UserNoAdminException(String message) {
        super(message);
    }
}
