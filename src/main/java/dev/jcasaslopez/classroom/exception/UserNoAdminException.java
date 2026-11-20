package dev.jcasaslopez.classroom.exception;

@SuppressWarnings("serial")
public class UserNoAdminException extends RuntimeException {
	public UserNoAdminException(String message) {
        super(message);
    }
}
