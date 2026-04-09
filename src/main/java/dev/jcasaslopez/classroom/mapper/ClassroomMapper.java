package dev.jcasaslopez.classroom.mapper;

import org.springframework.stereotype.Component;

import dev.jcasaslopez.classroom.dto.ClassroomDto;
import dev.jcasaslopez.classroom.entity.Classroom;
import dev.jcasaslopez.classroom.shared.event.ClassroomEvent;

@Component
public class ClassroomMapper {
	
	public Classroom classroomDtoToClassroom (ClassroomDto classroom) {
		return new Classroom(classroom.getIdClassroom(),
							classroom.getName(),
							classroom.getSeats(),
							classroom.getProjector(),
							classroom.getSpeakers());
	}
	
	public ClassroomDto classroomToClassroomDto (Classroom classroom) {
		return new ClassroomDto(classroom.getIdClassroom(),
							classroom.getName(),
							classroom.getSeats(),
							classroom.isProjector(),
							classroom.isSpeakers());
	}
	
	public ClassroomEvent classroomDtoToClassroomEvent (ClassroomDto classroom) {
		return new ClassroomEvent(classroom.getIdClassroom(),
				classroom.getName(),
				classroom.getSeats(),
				classroom.getProjector(),
				classroom.getSpeakers());
	}
	
	public ClassroomEvent classroomToClassroomEvent (Classroom classroom) {
		return new ClassroomEvent(classroom.getIdClassroom(),
				classroom.getName(),
				classroom.getSeats(),
				classroom.isProjector(),
				classroom.isSpeakers());
	}

}
