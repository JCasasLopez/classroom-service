package dev.jcasaslopez.classroom.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.jcasaslopez.classroom.enums.UserAuthenticationStatus;
import dev.jcasaslopez.classroom.exception.FailedAuthenticatedException;
import dev.jcasaslopez.classroom.exception.UserNoAdminException;
import dev.jcasaslopez.classroom.util.StandardResponseHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	private final RestClient restClient;
	private final StandardResponseHandler standardResponseHandler;

	public AuthenticationFilter(RestClient restClient, StandardResponseHandler standardResponseHandler) {
		this.restClient = restClient;
		this.standardResponseHandler = standardResponseHandler;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
//		  El filtro capta la petición HTTP que vendrá con el token JWT en el encabezado,
//		  y envía el token al microservicio "user" que responde con una enumeración: si el token no es
//		  válido o el usuario no es "admin", se lanza la excepción correspondiente; si la autenticación
//		  es correcta y el usuario es "admin", se continúa el flujo hacia el controlador.

//		  The filter captures the HTTP request that includes the JWT token in the header,
//		  and sends the token to the "user" microservice, which in turn will respond with an enumeration.
//		  If the token is not valid, or the user is not an "admin," an exception is thrown.
//        If the authentication is successful and the user is an "admin," the flow continues to the controllers.
		
		
		try {
			logger.debug("Entering AuthenticationFilter...");
			String baseUrl = "http://service-user/user/authenticateUser";
			String token = request.getHeader("Authorization");

//			Permitir un token especial solo para pruebas 
//			Allow a special token for testing 
			if ("Bearer test-token".equals(token)) {
				logger.info("Using test token for authentication");
				filterChain.doFilter(request, response);
				return;
			}

//			Evitamos hacer una llamada innecesaria al servicio users verificando aquí estas condiciones
//		    If these conditions are met, we can handle them here and avoid an unnecessary call to the "users" service 
			if (token == null || !token.startsWith("Bearer ")) {
				logger.warn("Token is missing or does not start with 'Bearer '");
				throw new FailedAuthenticatedException("Invalid authentication token");
			}

			UserAuthenticationStatus userAuthenticationStatus = restClient
					.get()
					.uri(baseUrl)
					.header("Authorization", token)
					.retrieve()
					.body(UserAuthenticationStatus.class);

			logger.info("UserAuthenticationStatus received: {}", userAuthenticationStatus);

			switch (userAuthenticationStatus) {
			case FAILED_AUTHENTICATION:
				logger.warn("Invalid or expired token");
				throw new FailedAuthenticatedException("Invalid or expired token");
			case USER_NO_ADMIN:
				logger.warn("User is authenticated but not an admin");
				throw new UserNoAdminException("User is authenticated but not an admin");
			case USER_ADMIN:
				logger.debug("User is an admin");
				filterChain.doFilter(request, response);
				break;
			default:
				logger.error("Unexpected authentication status: {}", userAuthenticationStatus);
				throw new IllegalStateException("Unexpected value: " + userAuthenticationStatus);
			}
			
		} catch (FailedAuthenticatedException ex) {
			standardResponseHandler.handleResponse(response, 401, ex.getMessage(), null);
			
		} catch (UserNoAdminException ex) {
			standardResponseHandler.handleResponse(response, 403, ex.getMessage(), null);
			
		} catch (IllegalStateException ex) {
			standardResponseHandler.handleResponse(response, 500, ex.getMessage(), null);
		}
	}
}