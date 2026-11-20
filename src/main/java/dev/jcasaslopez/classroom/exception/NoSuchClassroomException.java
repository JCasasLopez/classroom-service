package dev.jcasaslopez.classroom.exception;

@SuppressWarnings("serial")
public class NoSuchClassroomException extends RuntimeException {
	public NoSuchClassroomException(String message) {
        super(message);
    }
}