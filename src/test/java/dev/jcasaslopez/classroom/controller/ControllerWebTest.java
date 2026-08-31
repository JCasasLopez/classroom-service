package dev.jcasaslopez.classroom.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.jcasaslopez.classroom.dto.ClassroomRequestDto;
import dev.jcasaslopez.classroom.exception.GlobalExceptionHandler;
import dev.jcasaslopez.classroom.exception.NoSuchClassroomException;
import dev.jcasaslopez.classroom.filter.ClassroomAuthenticationFilter;
import dev.jcasaslopez.classroom.service.ClassroomService;
import dev.jcasaslopez.classroom.util.ClassroomEndpoints;

@WebMvcTest(
	    controllers = {ClassroomController.class, GlobalExceptionHandler.class},
	    
	    // Exclude the authentication filter from this slice, as configuration complexity skyrockets if included,
	    // and it is unrelated to DTO validation anyway; it is covered in a separate filter test.
	    excludeFilters = @ComponentScan.Filter(
	        type = FilterType.ASSIGNABLE_TYPE,
	        classes = ClassroomAuthenticationFilter.class
	    )
	)

public class ControllerWebTest {
	
	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
	@MockitoBean private ClassroomService service;
	
	@ParameterizedTest
	@MethodSource("invalidFields")
	void create_classroom_throws_exception_when_fields_invalid(String name, Integer seats, boolean projector, boolean speakers) throws JsonProcessingException, Exception {
		// Arrange
		ClassroomRequestDto invalidClassroom = new ClassroomRequestDto(name, seats, projector, speakers);
		
		// Act & Assert
		mockMvc.perform(post(ClassroomEndpoints.CLASSROOMS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidClassroom)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Missing or invalid fields"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
		
		verifyNoInteractions(service);
	}
	
	private static Stream<Arguments> invalidFields() {
		return Stream.of(
				// Classroom name over the 15 characters limit
				Arguments.of("Salón de Actos Miguel de Cervantes Saaveedra", 20, true, true),
				
				// Classroom name is blank
				Arguments.of("", 20, true, true),
				
				// Seats field is null 
				Arguments.of("101", null, true, true),
				
				// Seats is higher than the 300 max.
				Arguments.of("101", 500, true, true)
				);
	}
	
	@Test
	void create_or_update_classroom_when_duplicate_name_returns_409() throws JsonProcessingException, Exception {
		// Arrange
		ClassroomRequestDto duplicateNameClassroom = new ClassroomRequestDto("101", 50, true, true);
		when(service.createClassroom(duplicateNameClassroom)).thenThrow(new DataIntegrityViolationException("Duplicate name error messsage"));
		
		// Act & Assert
		mockMvc.perform(post(ClassroomEndpoints.CLASSROOMS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateNameClassroom)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("The database already contains a classroom with this name"))
                .andExpect(jsonPath("$.status").value("CONFLICT"));
		
		verify(service, times(1)).createClassroom(duplicateNameClassroom);
	}
	
	@Test
	void update_or_delete_classroom_not_found_returns_404() throws Exception {
		// Arrange
		int idClassroomNotFound = 1;
		doThrow(new NoSuchClassroomException("Classroom not found in the database")).when(service).deleteClassroom(idClassroomNotFound);
		
		// Act & Assert
		mockMvc.perform(delete(ClassroomEndpoints.CLASSROOM_BY_ID, idClassroomNotFound))                
				.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Classroom not found in the database"))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"));
		
		verify(service, times(1)).deleteClassroom(idClassroomNotFound);	
	}

}
