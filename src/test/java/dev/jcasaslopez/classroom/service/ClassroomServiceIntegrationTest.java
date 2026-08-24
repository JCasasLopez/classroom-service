package dev.jcasaslopez.classroom.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import dev.jcasaslopez.classroom.base.BaseRepositoryTest;
import dev.jcasaslopez.classroom.dto.ClassroomRequestDto;
import dev.jcasaslopez.classroom.mapper.ClassroomMapper;
import dev.jcasaslopez.classroom.producer.ClassroomEventProducer;
import dev.jcasaslopez.classroom.shared.event.ClassroomEvent;
import jakarta.persistence.EntityManager;

// The happy path is covered by a E2E test. Here we only cover the duplicate classroom name scenario:
// here we verify that the Kafka topic is not updated, and the database is not corrupted.
// We need to use a real database as DataIntegrityViolationException is thrown at the database level.

@ExtendWith(MockitoExtension.class)
public class ClassroomServiceIntegrationTest extends BaseRepositoryTest{
	
	@Mock ClassroomEventProducer kafkaProducer;
	private final static ClassroomRequestDto CLASSROOM_REQUEST = new ClassroomRequestDto("101", 30, true, false);
	private ClassroomService service;
	@Autowired private EntityManager entityManager;

	@BeforeEach
	void setUp() {
		// It would be more orthodox to mock the mapper as well, but since this is already a light integration test   
		// (database spun up with Testcontainers), we may as well use a real mapper too; the mapper is tested and it is 
		// used several times in the methods, so is safe to use and save ourselves a lot boilerplate code by using it.
		service = new ClassroomServiceImpl(repository, new ClassroomMapper(), kafkaProducer);
	}
	
	@Test
	void create_classroom_with_duplicate_name_neither_persists_nor_publishes() {
		// Arrange
		service.createClassroom(CLASSROOM_REQUEST);

		// Act & Assert
		assertThrows(DataIntegrityViolationException.class, () -> service.createClassroom(CLASSROOM_REQUEST));
		
		// Clears invalid entities from the Persistence Context before querying the database
		entityManager.clear();
	    assertEquals(1, repository.count());
		verify(kafkaProducer, times(1)).publishClassroom(any(ClassroomEvent.class));	
	}
	
}