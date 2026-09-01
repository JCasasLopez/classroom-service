package dev.jcasaslopez.classroom.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.jcasaslopez.classroom.base.BaseIntegrationTest;
import dev.jcasaslopez.classroom.dto.ClassroomRequestDto;
import dev.jcasaslopez.classroom.dto.ClassroomResponseDto;
import dev.jcasaslopez.classroom.entity.Classroom;
import dev.jcasaslopez.classroom.shared.utility.StandardResponse;
import dev.jcasaslopez.classroom.util.IntegrationTestHelper;

// Because of the different nature of HTTP responses (synchronous) and Kafka-topic writing (asynchronous),
// the latter is tested in a different class (KafkaProducerIntegrationTest).

public class ControllerIntegrationTest extends BaseIntegrationTest {
	
	private static final ClassroomRequestDto CLASSROOM = new ClassroomRequestDto("101", 50, true, true);
	private static final ClassroomRequestDto CLASSROOM2 = new ClassroomRequestDto("Isaac Newton", 70, true, true);
	
	@Test
	void happy_path_for_create_classroom_endpoint() {
		// Arrange
		
		// Act
		ResponseEntity<StandardResponse<Void>> response = IntegrationTestHelper.createClassroom(CLASSROOM);
		
		// Assert
		Classroom savedClassroom = repository.findAll().get(0);

		assertAll(
				() -> assertEquals(1, repository.count()),
				() -> assertEquals(CLASSROOM.name(), savedClassroom.getName()),
				() -> assertEquals(CLASSROOM.seats(), savedClassroom.getSeats()),
				() -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
				() -> assertEquals("Classroom created successfully", response.getBody().message())
				);
	}
	
	@Test
	void happy_path_for_delete_classroom_endpoint() {
		// Arrange
		IntegrationTestHelper.createClassroom(CLASSROOM);
		
		// Act
		Classroom savedClassroom = repository.findAll().get(0);
		ResponseEntity<StandardResponse<Void>> response = IntegrationTestHelper.deleteClassroom(savedClassroom.getIdClassroom());
		
		// Assert
		assertAll(
				() -> assertEquals(0, repository.count()),
				() -> assertEquals(HttpStatus.OK, response.getStatusCode()),
				() -> assertEquals("Classroom deleted successfully", response.getBody().message())
				);
	}
	
	@Test
	void happy_path_for_update_classroom_endpoint() {
		// Arrange
		IntegrationTestHelper.createClassroom(CLASSROOM);
		
		// Act
		Classroom savedClassroom = repository.findAll().get(0);
		ResponseEntity<StandardResponse<Void>> response = IntegrationTestHelper.updateClassroom(CLASSROOM2, savedClassroom.getIdClassroom());
		Classroom updatedClassroom = repository.findAll().get(0);
		
		// Assert
		assertAll(
				() -> assertEquals(1, repository.count()),
				() -> assertEquals(CLASSROOM2.name(), updatedClassroom.getName()),
				() -> assertEquals(CLASSROOM2.seats(), updatedClassroom.getSeats()),
				() -> assertEquals(HttpStatus.OK, response.getStatusCode()),
				() -> assertEquals("Classroom updated successfully", response.getBody().message())
				);
	}
	
	@Test
	void happy_path_for_getClassroomsList_endpoint() {
		// Arrange
		IntegrationTestHelper.createClassroom(CLASSROOM);
		IntegrationTestHelper.createClassroom(CLASSROOM2);

		// Act
		ResponseEntity<StandardResponse<List<ClassroomResponseDto>>> response = IntegrationTestHelper.getClassroomList();
		List<ClassroomResponseDto> classrooms = response.getBody().details();

		
		// Assert
		assertAll(
			    () -> assertEquals(2, repository.count()),
			    () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
			    () -> assertEquals("Classrooms list retrieved successfully", response.getBody().message()),
			    () -> assertEquals(2, classrooms.size()),
			    () -> assertTrue(classrooms.stream().anyMatch(c -> 
			            c.name().equals(CLASSROOM.name()) && c.seats() == CLASSROOM.seats())),
			    () -> assertTrue(classrooms.stream().anyMatch(c -> 
			            c.name().equals(CLASSROOM2.name()) && c.seats() == CLASSROOM2.seats()))
			);
	}
}