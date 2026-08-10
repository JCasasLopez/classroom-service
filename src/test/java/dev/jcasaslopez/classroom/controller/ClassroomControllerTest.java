package dev.jcasaslopez.classroom.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.jcasaslopez.classroom.dto.ClassroomDto;
import dev.jcasaslopez.classroom.service.ClassroomService;
import dev.jcasaslopez.classroom.shared.utility.StandardResponse;

// @WebMvcTest automatically loads various MVC web-layer components, including filters, 
// so there's no need to explicitly mock them.
@WebMvcTest
public class ClassroomControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockitoBean
	private ClassroomService classroomService;
	
	@Test
	@DisplayName("201 CREATED when creating classroom successfully")
	void createClassroom_ValidRequest_Status201() throws Exception {
		//Arrange
		ClassroomDto classroomDto = new ClassroomDto (0, "Classroom A", 20, true, true);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createClassroom")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer test-token")
				.content(objectMapper.writeValueAsString(classroomDto));
		
		//Act
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
		String responseBodyAsString = mvcResult.getResponse().getContentAsString();
		StandardResponse<Void> response = objectMapper.readValue(
			    responseBodyAsString,
			    new TypeReference<StandardResponse<Void>>() {}
			);
		
		//Assert
		assertAll("Validate returned StandardResponse properties",
		        () -> assertEquals(HttpStatus.CREATED, response.status(), "HTTP status should match"),
		        () -> assertEquals("Classroom created successfully", response.message(), "Messages should match"),
		        () -> assertNull(response.details(), "Details should be null")
		    );
		
		verify(classroomService).createClassroom(Mockito.any(ClassroomDto.class));
	}
	
	@Test
	@DisplayName("400 BAD REQUEST when creating classroom with missing fields")
	void createClassroom_NonValidRequest_Status400() throws Exception {
		//Arrange
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createClassroom")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer test-token")
				.content("{\"idClassroom\":0,\"name\":\"Classroom A\",\"speakers\":true}");
		
		//Act
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
		String responseBodyAsString = mvcResult.getResponse().getContentAsString();
		StandardResponse<Void> response = objectMapper.readValue(
			    responseBodyAsString,
			    new TypeReference<StandardResponse<Void>>() {}
			);
		
		//Assert
		assertAll("Validate returned StandardResponse properties",
		        () -> assertEquals(HttpStatus.BAD_REQUEST, response.status(), "HTTP status should match"),
		        () -> assertEquals("Missing or invalid fields", response.message(), "Messages should match"),
		        () -> assertNull(response.details(), "Details should be null")
		    );
		
		verify(classroomService, never()).createClassroom(Mockito.any(ClassroomDto.class));
	}
	
	@Test
	@DisplayName("200 OK when deleting classroom successfully")
	void deleteClassroom_ValidRequest_Status200() throws Exception {
		//Arrange
		int idClasroom = 1;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/deleteClassroom")
				.queryParam("idClassroom", String.valueOf(idClasroom))
				.header("Authorization", "Bearer test-token");
		
		//Act
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
		String responseBodyAsString = mvcResult.getResponse().getContentAsString();
		StandardResponse<Void> response = objectMapper.readValue(
			    responseBodyAsString,
			    new TypeReference<StandardResponse<Void>>() {}
			);		
		
		//Assert
		assertAll("Validate returned StandardResponse properties",
		        () -> assertEquals(HttpStatus.OK, response.status(), "HTTP status should match"),
		        () -> assertEquals("Classroom deleted successfully", response.message(), "Messages should match"),
		        () -> assertNull(response.details(), "Details should be null")
		    );
		
		verify(classroomService).deleteClassroom(idClasroom);
	}
	
	@Test
	@DisplayName("200 OK when updating classroom successfully")
	void updateClassroom_ValidRequest_Status200() throws Exception {
		//Arrange
		ClassroomDto classroomDto = new ClassroomDto (0, "Classroom A", 20, true, true);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/updateClassroom")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer test-token")
				.content(objectMapper.writeValueAsString(classroomDto));
		
		//Act
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
		String responseBodyAsString = mvcResult.getResponse().getContentAsString();
		StandardResponse<Void> response = objectMapper.readValue(
			    responseBodyAsString,
			    new TypeReference<StandardResponse<Void>>() {}
			);
		
		//Assert
		assertAll("Validate returned StandardResponse properties",
		        () -> assertEquals(HttpStatus.OK, response.status(), "HTTP status should match"),
		        () -> assertEquals("Classroom updated successfully", response.message(), "Messages should match"),
		        () -> assertNull(response.details(), "Details should be null")
		    );
		
		verify(classroomService).updateClassroom(Mockito.any(ClassroomDto.class));
	}
	
	@Test
	@DisplayName("400 BAD REQUEST when updating classroom with missing fields")
	void updateClassroom_NonValidRequest_Status400() throws Exception {
		//Arrange
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/updateClassroom")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer test-token")
				.content("{\"idClassroom\":0,\"name\":\"Classroom A\",\"speakers\":true}");
		
		//Act
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
		String responseBodyAsString = mvcResult.getResponse().getContentAsString();
		StandardResponse<Void> response = objectMapper.readValue(
			    responseBodyAsString,
			    new TypeReference<StandardResponse<Void>>() {}
			);
		
		//Assert
		assertAll("Validate returned StandardResponse properties",
		        () -> assertEquals(HttpStatus.BAD_REQUEST, response.status(), "HTTP status should match"),
		        () -> assertEquals("Missing or invalid fields", response.message(), "Messages should match"),
		        () -> assertNull(response.details(), "Details should be null")
		    );
		
		verify(classroomService, never()).updateClassroom(Mockito.any(ClassroomDto.class));
	}
}
