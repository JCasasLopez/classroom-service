package dev.jcasaslopez.classroom.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.jcasaslopez.classroom.dto.ClassroomDto;
import dev.jcasaslopez.classroom.entity.Classroom;

/* Estas anotaciones permiten evitar la carga completa del contexto con @SpringBootTest.  
En su lugar, inicializamos solo ClassroomMapper y la configuración mínima necesaria.  
---------------------------------------------------------------------------------
Instead of loading the full context with @SpringBootTest, we initialize only  
ClassroomMapper and the minimal required configuration. */

@ExtendWith(SpringExtension.class)
@Import(ClassroomMapper.class)
public class ClassroomMapperTest {
	
	@Autowired
	private ClassroomMapper classroomMapper;
	
	@Test
	@DisplayName("ClassroomDto to Classroom mapped successfully")
	void classroomDtoToClassroom_ClassroomDtoMappedSuccessfully() {
		// Arrange
		ClassroomDto classroomDto = new ClassroomDto (1, "Classroom A", 20, true, true);
		
		// Act
		Classroom mappedClassroom = classroomMapper.classroomDtoToClassroom(classroomDto);
		
		// Assert
		assertAll("Validate mapped Classroom properties",
				() -> assertEquals(classroomDto.getIdClassroom(), mappedClassroom.getIdClassroom(), "IDs should match"),
				() -> assertEquals(classroomDto.getName(), mappedClassroom.getName(), "Names should match"),
				() -> assertEquals(classroomDto.getSeats(), mappedClassroom.getSeats(), "Number of seats should match"),
				() -> assertEquals(classroomDto.isProjector(), mappedClassroom.isProjector(), "Projector status should match"),
				() -> assertEquals(classroomDto.isSpeakers(), mappedClassroom.isSpeakers(), "Speakers status should match")
				);
	}
	
	@Test
	@DisplayName("Classroom to ClassroomDto mapped successfully")
	void classroomToClassroomDto_ClassroomMappedSuccessfully() {
		// Arrange
		Classroom classroom = new Classroom (1, "Classroom A", 20, true, true);
		
		// Act
		ClassroomDto mappedClassroomDto = classroomMapper.classroomToClassroomDto(classroom);
		
		// Assert
		assertAll("Validate mapped ClassroomDto properties",
				() -> assertEquals(classroom.getIdClassroom(), mappedClassroomDto.getIdClassroom(), "IDs should match"),
				() -> assertEquals(classroom.getName(), mappedClassroomDto.getName(), "Names should match"),
				() -> assertEquals(classroom.getSeats(), mappedClassroomDto.getSeats(), "Number of seats should match"),
				() -> assertEquals(classroom.isProjector(), mappedClassroomDto.isProjector(), "Projector status should match"),
				() -> assertEquals(classroom.isSpeakers(), mappedClassroomDto.isSpeakers(), "Speakers status should match")
				);
	}
}
