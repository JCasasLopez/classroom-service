package dev.jcasaslopez.classroom.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.jcasaslopez.classroom.dto.ClassroomDto;
import dev.jcasaslopez.classroom.entity.Classroom;
import dev.jcasaslopez.classroom.exception.NoSuchClassroomException;
import dev.jcasaslopez.classroom.mapper.ClassroomMapper;
import dev.jcasaslopez.classroom.repository.ClassroomRepository;

@ExtendWith(MockitoExtension.class)
class ClassroomServiceTest {

	@Mock
	private ClassroomRepository classroomRepository;

	@Mock
	private ClassroomMapper classroomMapper;
	
	@InjectMocks
	private ClassroomServiceImpl classroomService;

	@Test
	@DisplayName("Classroom created successfully")
	void createClassroom_ShouldReturnSavedClassroom() {
		//Arrange
		ClassroomDto classroomDto = new ClassroomDto (1, "Classroom A", 20, true, true);
		Classroom classroom = new Classroom (1, "Classroom A", 20, true, true);
		when(classroomMapper.classroomDtoToClassroom(classroomDto)).thenReturn(classroom);
		when(classroomRepository.save(classroom)).thenReturn(classroom);
		when(classroomMapper.classroomToClassroomDto(classroom)).thenReturn(classroomDto);

		//Act
		ClassroomDto returnedClassroom = classroomService.createClassroom(classroomDto);

		//Assert
		assertAll("Validate returned ClassroomDto properties",
				() -> assertEquals(classroomDto.getName(), returnedClassroom.getName(), "Names should match"),
				() -> assertEquals(classroomDto.getSeats(), returnedClassroom.getSeats(), "Number of seats should match"),
				() -> assertEquals(classroomDto.isProjector(), returnedClassroom.isProjector(), "Projector status should match"),
				() -> assertEquals(classroomDto.isSpeakers(), returnedClassroom.isSpeakers(), "Speakers status should match")
				);

		InOrder inOrder = inOrder(classroomMapper, classroomRepository);
		inOrder.verify(classroomMapper).classroomDtoToClassroom(classroomDto);
		inOrder.verify(classroomRepository).save(classroom);
		inOrder.verify(classroomMapper).classroomToClassroomDto(classroom);
	}

	@Test
	@DisplayName("Should delete an existing classroom successfully")
	void deleteClassroom_WhenClassroomExists_DeletesSuccessfully() {
		//Arrange
		int idClassroom = 1;
		Optional<Classroom> classroom = Optional.of(new Classroom (1, "Classroom A", 20, true, true));
		when(classroomRepository.findById(idClassroom)).thenReturn(classroom);

		//Act
		classroomService.deleteClassroom(idClassroom);

		//Assert
		InOrder inOrder = inOrder(classroomRepository);
		inOrder.verify(classroomRepository).findById(idClassroom);
		inOrder.verify(classroomRepository).deleteById(idClassroom);
	}

	@Test
	@DisplayName("Delete - Throws NoSuchClassroomException when the classroom does not exist")
	void deleteClassroom_WhenNotFound_ThrowsException() {
		//Arrange
		int idClassroom = 1;
		when(classroomRepository.findById(idClassroom)).thenReturn(Optional.empty());

		//Act & Assert
		assertThrows(NoSuchClassroomException.class, 
				() -> classroomService.deleteClassroom(idClassroom));
		verify(classroomRepository).findById(idClassroom);
		verify(classroomRepository, never()).deleteById(idClassroom);
	}

	@Test
	@DisplayName("Should update an existing classroom successfully")
	void updateClassroom_WhenClassroomExists_UpdatesSuccessfully() {
		//Arrange
		int idClassroom = 1;
		ClassroomDto classroomDto = new ClassroomDto (1, "Classroom Red", 25, false, true);
		Optional<Classroom> foundClassroom = Optional.of(new Classroom (1, "Classroom A", 20, true, true));
		Classroom classroom = new Classroom (1, "Classroom Red", 25, false, true);

		when(classroomRepository.findById(idClassroom)).thenReturn(foundClassroom);
		when(classroomMapper.classroomDtoToClassroom(classroomDto)).thenReturn(classroom);
		when(classroomRepository.save(classroom)).thenReturn(classroom);
		when(classroomMapper.classroomToClassroomDto(classroom)).thenReturn(classroomDto);

		//Act
		ClassroomDto returnedClassroom = classroomService.updateClassroom(classroomDto);

		//Assert
		assertAll("Validate returned ClassroomDto properties",
				() -> assertEquals(classroomDto.getName(), returnedClassroom.getName(), "Names should match"),
				() -> assertEquals(classroomDto.getSeats(), returnedClassroom.getSeats(), "Number of seats should match"),
				() -> assertEquals(classroomDto.isProjector(), returnedClassroom.isProjector(), "Projector status should match"),
				() -> assertEquals(classroomDto.isSpeakers(), returnedClassroom.isSpeakers(), "Speakers status should match")
				);

		InOrder inOrder = inOrder(classroomRepository, classroomMapper);
		inOrder.verify(classroomRepository).findById(idClassroom);
		inOrder.verify(classroomMapper).classroomDtoToClassroom(classroomDto);
		inOrder.verify(classroomRepository).save(classroom);
		inOrder.verify(classroomMapper).classroomToClassroomDto(classroom);
	}

	@Test
	@DisplayName("Update - Throws NoSuchClassroomException when the classroom does not exist")
	void updateClassroom_WhenNotFound_ThrowsException() {
		//Arrange
		int idClassroom = 1;
		ClassroomDto classroomDto = new ClassroomDto (1, "Classroom Red", 25, false, true);
		Optional<Classroom> foundClassroom = Optional.empty();

		when(classroomRepository.findById(idClassroom)).thenReturn(foundClassroom);

		//Act & Assert
		assertThrows(NoSuchClassroomException.class, 
				() -> classroomService.updateClassroom(classroomDto));
		verify(classroomRepository).findById(idClassroom);
		verify(classroomRepository, never()).save(any(Classroom.class));
	}

}
