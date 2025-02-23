package dev.jcasaslopez.classroom.service;

import dev.jcasaslopez.classroom.dto.ClassroomDto;

public interface ClassroomService {
	
	ClassroomDto createClassroom(ClassroomDto classroom);
	void deleteClassroom(int idClassroom);
	ClassroomDto updateClassroom(ClassroomDto classroom);
	
}
