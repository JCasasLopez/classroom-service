package dev.jcasaslopez.classroom.service;

import java.util.List;

import dev.jcasaslopez.classroom.dto.ClassroomDto;

public interface ClassroomService {
	
	ClassroomDto createClassroom(ClassroomDto classroom);
	void deleteClassroom(int idClassroom);
	ClassroomDto updateClassroom(ClassroomDto classroom);
	List<ClassroomDto> findAll();
	public void publishAllClassrooms();
	
}