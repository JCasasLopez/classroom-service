package dev.jcasaslopez.classroom.service;

import java.util.Optional;

import dev.jcasaslopez.classroom.dto.ClassroomDto;

public interface ClassroomService {
	
	ClassroomDto createClassroom(ClassroomDto classroom);
	Optional<ClassroomDto> removeClassroom(int idClassroom);
	Optional<ClassroomDto> updateClassroom(ClassroomDto classroom);

}
