package dev.jcasaslopez.classroom.mapper;

import org.springframework.stereotype.Component;

import dev.jcasaslopez.classroom.dto.ClassroomRequestDto;
import dev.jcasaslopez.classroom.dto.ClassroomResponseDto;
import dev.jcasaslopez.classroom.entity.Classroom;
import dev.jcasaslopez.classroom.shared.event.ClassroomEvent;

@Component
public class ClassroomMapper {

    public Classroom requestToEntity(ClassroomRequestDto classroom) {
        return new Classroom(
                0,
                classroom.name(),
                classroom.seats(),
                classroom.projector(),
                classroom.speakers());
    }

    public ClassroomResponseDto entityToResponse(Classroom classroom) {
        return new ClassroomResponseDto(
                classroom.getIdClassroom(),
                classroom.getName(),
                classroom.getSeats(),
                classroom.isProjector(),
                classroom.isSpeakers());
    }

    public ClassroomEvent responseToClassroomEvent(ClassroomResponseDto classroom) {
        return new ClassroomEvent(
                classroom.idClassroom(),
                classroom.name(),
                classroom.seats(),
                classroom.projector(),
                classroom.speakers());
    }

    public ClassroomEvent entityToClassroomEvent(Classroom classroom) {
        return new ClassroomEvent(
                classroom.getIdClassroom(),
                classroom.getName(),
                classroom.getSeats(),
                classroom.isProjector(),
                classroom.isSpeakers());
    }
}