package dev.jcasaslopez.classroom.filter;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import dev.jcasaslopez.classroom.exception.FailedAuthenticatedException;
import jakarta.servlet.FilterChain;

@ExtendWith(MockitoExtension.class)
public class AuthenticationFilterTest {
	
    @Mock
    private FilterChain filterChain;
    
    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setup() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }
	
	@Test
	@DisplayName("When token is null throws FailedAuthenticatedException")
	void doFilterInternal_WhenTokenNull_ThrowsEception() {
		// Arrange
		/* Si intentamos asignar "null" al encabezado, da un error; si no asignamos ningún valor
		   al encabezado, devuelve "null" y el test funciona correctamente.
		   -------------------------------------------------------------------------------------
		   If "null" is set as the header value, the test fails; as it is in the code below, without 
		   setting any value to the header, the code returns "null" and the test works. */
      
        // Act & Assert
        assertThrows(FailedAuthenticatedException.class,
            () -> authenticationFilter.doFilterInternal(request, response, filterChain));
	}
	
	@Test
    @DisplayName("When token doesn't start with 'Bearer' throws FailedAuthenticatedException")
    void doFilterInternal_WhenTokenDoesntStartWithBearer_ThrowsException() {
        // Arrange
        request.addHeader("Authorization", "Invalid-Token");

        // Act & Assert
        assertThrows(FailedAuthenticatedException.class,
            () -> authenticationFilter.doFilterInternal(request, response, filterChain));
    }
}