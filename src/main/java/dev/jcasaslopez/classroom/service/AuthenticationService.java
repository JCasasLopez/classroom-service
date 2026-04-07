package dev.jcasaslopez.classroom.service;

public interface AuthenticationService {
	
	boolean validateToken(String authHeader);
	
}
