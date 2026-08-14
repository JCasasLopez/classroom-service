package dev.jcasaslopez.classroom.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.jcasaslopez.classroom.dto.ClassroomRequestDto;
import dev.jcasaslopez.classroom.dto.ClassroomResponseDto;
import dev.jcasaslopez.classroom.entity.Classroom;
import dev.jcasaslopez.classroom.shared.event.ClassroomEvent;

class ClassroomMapperTest {

    private final ClassroomMapper classroomMapper = new ClassroomMapper();

    @Test
    @DisplayName("ClassroomRequestDto to Classroom mapped successfully")
    void requestToEntity_MappedSuccessfully() {
        // Arrange
        ClassroomRequestDto request = new ClassroomRequestDto("Classroom A", 20, true, true);

        // Act
        Classroom mappedClassroom = classroomMapper.requestToEntity(request);

        // Assert
        assertAll("Validate mapped Classroom properties",
                () -> assertEquals(0, mappedClassroom.getIdClassroom(), "ID should be forced to 0 on creation"),
                () -> assertEquals(request.name(), mappedClassroom.getName(), "Names should match"),
                () -> assertEquals(request.seats(), mappedClassroom.getSeats(), "Number of seats should match"),
                () -> assertEquals(request.projector(), mappedClassroom.isProjector(), "Projector status should match"),
                () -> assertEquals(request.speakers(), mappedClassroom.isSpeakers(), "Speakers status should match")
        );
    }

    @Test
    @DisplayName("Classroom to ClassroomResponseDto mapped successfully")
    void entityToResponse_MappedSuccessfully() {
        // Arrange
        Classroom classroom = new Classroom(1, "Classroom A", 20, true, true);

        // Act
        ClassroomResponseDto mappedResponse = classroomMapper.entityToResponse(classroom);

        // Assert
        assertAll("Validate mapped ClassroomResponseDto properties",
                () -> assertEquals(classroom.getIdClassroom(), mappedResponse.idClassroom(), "IDs should match"),
                () -> assertEquals(classroom.getName(), mappedResponse.name(), "Names should match"),
                () -> assertEquals(classroom.getSeats(), mappedResponse.seats(), "Number of seats should match"),
                () -> assertEquals(classroom.isProjector(), mappedResponse.projector(), "Projector status should match"),
                () -> assertEquals(classroom.isSpeakers(), mappedResponse.speakers(), "Speakers status should match")
        );
    }

    @Test
    @DisplayName("ClassroomResponseDto to ClassroomEvent mapped successfully")
    void responseToClassroomEvent_MappedSuccessfully() {
        // Arrange
        ClassroomResponseDto response = new ClassroomResponseDto(1, "Classroom A", 20, true, true);

        // Act
        ClassroomEvent mappedEvent = classroomMapper.responseToClassroomEvent(response);

        // Assert
        assertAll("Validate mapped ClassroomEvent properties",
                () -> assertEquals(response.idClassroom(), mappedEvent.getIdClassroom(), "IDs should match"),
                () -> assertEquals(response.name(), mappedEvent.getName(), "Names should match"),
                () -> assertEquals(response.seats(), mappedEvent.getSeats(), "Number of seats should match"),
                () -> assertEquals(response.projector(), mappedEvent.getProjector(), "Projector status should match"),
                () -> assertEquals(response.speakers(), mappedEvent.getSpeakers(), "Speakers status should match")
        );
    }

    @Test
    @DisplayName("Classroom to ClassroomEvent mapped successfully")
    void entityToClassroomEvent_MappedSuccessfully() {
        // Arrange
        Classroom classroom = new Classroom(1, "Classroom A", 20, true, true);

        // Act
        ClassroomEvent mappedEvent = classroomMapper.entityToClassroomEvent(classroom);

        // Assert
        assertAll("Validate mapped ClassroomEvent properties",
                () -> assertEquals(classroom.getIdClassroom(), mappedEvent.getIdClassroom(), "IDs should match"),
                () -> assertEquals(classroom.getName(), mappedEvent.getName(), "Names should match"),
                () -> assertEquals(classroom.getSeats(), mappedEvent.getSeats(), "Number of seats should match"),
                () -> assertEquals(classroom.isProjector(), mappedEvent.getProjector(), "Projector status should match"),
                () -> assertEquals(classroom.isSpeakers(), mappedEvent.getProjector(), "Speakers status should match")
        );
    }
}