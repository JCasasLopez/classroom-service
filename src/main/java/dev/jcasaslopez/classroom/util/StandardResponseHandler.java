package dev.jcasaslopez.classroom.util;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dev.jcasaslopez.classroom.dto.StandardResponse;
import jakarta.servlet.http.HttpServletResponse;

//  StandardResponseHandler converts a StandardResponse into a standard HTTP response, which 
//  is necessary in classes like AuthenticationFilter, where StandardResponse cannot be used 
//  directly, as exceptions thrown in a filter are not handled by GlobalExceptionHandler.

@Component
public class StandardResponseHandler {
	
	ObjectMapper objectMapper;

	public StandardResponseHandler() {

		//  Creates an instance of ObjectMapper to convert Java objects to JSON and vice versa. 
		this.objectMapper = new ObjectMapper();
		
		//	Registers the module to properly handle date/time types (LocalDateTime, etc.)
		objectMapper.registerModule(new JavaTimeModule());
		
		//	Ensures that dates are stored as text in ISO-8601 format (e.g., "2024-02-22T15:30:00")
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}
    
    public HttpServletResponse handleResponse(HttpServletResponse response, int status, 
    																String message, String details) throws IOException {
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
    	response.setStatus(status);

    	// Creates a StandardResponse with the provided information.
    	StandardResponse respuesta = new StandardResponse (LocalDateTime.now(), message, details, 
    																HttpStatus.resolve(status));
    	// Serializes the StandardResponse into JSON format.
    	String jsonResponse = objectMapper.writeValueAsString(respuesta);
    	
    	// Writes the JSON response to the HTTP response body.
        response.getWriter().write(jsonResponse);
        return response;
    }
    
}
