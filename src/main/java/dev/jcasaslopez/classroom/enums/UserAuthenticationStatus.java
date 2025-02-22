package dev.jcasaslopez.classroom.enums;

public enum UserAuthenticationStatus {
	//  Token inválido o expirado
	//  Invalid or expired token
	FAILED_AUTHENTICATION,

	//	Usuario autenticado (no admin)
	//	User is authenticated (no admin) 
	USER_NO_ADMIN, 		

	//	Usuario autenticado (admin)
	//	User is authenticated (admin)
	USER_ADMIN            	
}
