package dev.jcasaslopez.classroom.filter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.jcasaslopez.classroom.shared.enums.RoleName;
import dev.jcasaslopez.classroom.shared.enums.TokenType;
import dev.jcasaslopez.classroom.shared.security.JwtService;
import dev.jcasaslopez.classroom.shared.utility.PublicSwaggerPaths;
import dev.jcasaslopez.classroom.util.ClassroomEndpoints;
import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class ClassroomAuthenticationFilterUnitTest {

	@Mock JwtService jwtService;
	@Mock HttpServletRequest request;

	private static final String secretKey = "...";
	private ClassroomAuthenticationFilter filter;

	@BeforeEach
	void setUp() {
		filter = new ClassroomAuthenticationFilter(jwtService, secretKey);
	}

	@ParameterizedTest
	@ValueSource(strings = {
			ClassroomEndpoints.GENERATE_TOKEN,
			PublicSwaggerPaths.SWAGGER_UI,
			PublicSwaggerPaths.API_DOCS
	})
	void shouldNotFilter_returns_true_for_excluded_paths(String path) {
		// Arrange
		when(request.getRequestURI()).thenReturn(path);
		
		// Act & Assert
		assertTrue(filter.shouldNotFilter(request));
	}

	@Test
	void shouldNotFilter_returns_false_for_protected_paths() {
		// Arrange
		when(request.getRequestURI()).thenReturn("/classrooms/1");
		
		// Act & Assert
		assertFalse(filter.shouldNotFilter(request));
	}

	@Test
	void validateToken_delegates_with_ACCESS_type_and_ADMIN_role() {
	    // Arrange
	    String authHeader = "Bearer valid-token";

	    // Act
	    filter.validateToken(authHeader);

	    // Assert
	    verify(jwtService).validateJwt(authHeader, secretKey, TokenType.ACCESS, List.of(RoleName.ROLE_ADMIN));
	}
}