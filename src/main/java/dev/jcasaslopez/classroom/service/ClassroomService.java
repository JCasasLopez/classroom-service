package dev.jcasaslopez.classroom.service;

import java.util.List;

import dev.jcasaslopez.classroom.dto.ClassroomRequestDto;
import dev.jcasaslopez.classroom.dto.ClassroomResponseDto;

public interface ClassroomService {
	
	ClassroomResponseDto createClassroom(ClassroomRequestDto classroom);
	void deleteClassroom(int idClassroom);
	ClassroomResponseDto updateClassroom(int idClassroom, ClassroomRequestDto classroom);
	List<ClassroomResponseDto> findAll();
}