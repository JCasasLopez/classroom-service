package dev.jcasaslopez.classroom.mapper;

import org.springframework.stereotype.Component;

import dev.jcasaslopez.classroom.dto.ClassroomDto;
import dev.jcasaslopez.classroom.entity.Classroom;

@Component
public class ClassroomMapper {
	
	public Classroom classroomDtoToClassroom (ClassroomDto classroom) {
		return new Classroom(classroom.getIdClassroom(),
							classroom.getName(),
							classroom.getSeats(),
							classroom.isProjector(),
							classroom.isSpeakers());
	}
	
	public ClassroomDto classroomToClassroomDto (Classroom classroom) {
		return new ClassroomDto(classroom.getIdClassroom(),
							classroom.getName(),
							classroom.getSeats(),
							classroom.isProjector(),
							classroom.isSpeakers());
	}

}
