package dev.jcasaslopez.classroom.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.jcasaslopez.classroom.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {


	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
	private final AuthenticationService authService;

	public AuthenticationFilter(AuthenticationService authService) {
		this.authService = authService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// The filter captures the HTTP request that includes the JWT token in the header,
		// and sends the token to the "user" microservice, which in turn will respond with an enumeration.
		// If the token is not valid, or the user is not an "admin," an exception is thrown.
		// If the authentication is successful and the user is an "admin," the flow continues to the controllers.
		logger.debug("Entering AuthenticationFilter...");
		String authHeader = request.getHeader("Authorization");

		// Allow a special token for testing
		if ("Bearer test-token".equals(authHeader)) {
			logger.info("Using test token for authentication");
			filterChain.doFilter(request, response);
			return;
		}

		// Exceptions thrown in a Filter are NOT caught by @RestControllerAdvice because Filters sit outside the Spring
		// DispatcherServlet context, so we use response.sendError() to manually trigger a 401 Unauthorized response.
		if(authService.validateToken(authHeader)) {
			filterChain.doFilter(request, response);
		} else {
			response.sendError(401, "Authentication failed");
		}
		return;

	}

}