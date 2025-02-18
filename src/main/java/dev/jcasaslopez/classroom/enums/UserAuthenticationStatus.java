package dev.jcasaslopez.classroom.enums;

public enum UserAuthenticationStatus {
	FAILED_AUTHENTICATION, 	//Token inválido o expirado --- Invalid or expired token
	USER_NO_ADMIN, 			/*Usuario autenticado (no admin)
									--- User is authenticated (no admin) */
	USER_ADMIN            	//Usuario autenticado (admin) --- User is authenticated (admin)
}
