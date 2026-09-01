package dev.jcasaslopez.classroom.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.jcasaslopez.classroom.dto.ClassroomRequestDto;
import dev.jcasaslopez.classroom.exception.NoSuchClassroomException;
import dev.jcasaslopez.classroom.mapper.ClassroomMapper;
import dev.jcasaslopez.classroom.producer.ClassroomEventProducer;
import dev.jcasaslopez.classroom.repository.ClassroomRepository;

// The happy path for each operation is covered by the E2E tests. Here we only cover the classroom not found 
// scenarios, and verify that the Kafka topic is not updated. There is nothing to check on the database, since 
// no entity exists to be affected.

@ExtendWith(MockitoExtension.class)
public class ClassroomServiceUnitTest {
	
	@Mock ClassroomEventProducer kafkaProducer;
	@Mock ClassroomRepository repository;
	@Mock ClassroomMapper mapper;
	
	private final static ClassroomRequestDto CLASSROOM_REQUEST = new ClassroomRequestDto("101", 30, true, false);
	private static final int ID_CLASSROOM = 1;
	private ClassroomService service;
	
	@BeforeEach
	void setUp() {
		service = new ClassroomServiceImpl(repository, mapper, kafkaProducer);
	}
	
	@Test
	void delete_classroom_throws_exception_and_does_not_publish_when_classroom_not_found() {
		// Arrange
		when(repository.findById(ID_CLASSROOM)).thenReturn(Optional.empty());
		
		// Act & Assert
		assertThrows(NoSuchClassroomException.class, () -> service.deleteClassroom(ID_CLASSROOM));
		verifyNoInteractions(kafkaProducer);
	}

	@Test
	void update_classroom_throws_exception_and_does_not_publish_when_classroom_not_found() {
		// Arrange
		when(repository.findById(ID_CLASSROOM)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(NoSuchClassroomException.class, () -> service.updateClassroom(ID_CLASSROOM, CLASSROOM_REQUEST));
		verifyNoInteractions(kafkaProducer);
	}

}
