package dev.jcasaslopez.classroom.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.jcasaslopez.classroom.dto.ClassroomDto;
import dev.jcasaslopez.classroom.entity.Classroom;
import dev.jcasaslopez.classroom.exception.NoSuchClassroomException;
import dev.jcasaslopez.classroom.mapper.ClassroomMapper;
import dev.jcasaslopez.classroom.repository.ClassroomRepository;

@Service
public class ClassroomServiceImpl implements ClassroomService {
	
	private final ClassroomRepository classroomRepository;
	private final ClassroomMapper classroomMapper;
	
	public ClassroomServiceImpl(ClassroomRepository classroomRepository, ClassroomMapper classroomMapper) {
		this.classroomRepository = classroomRepository;
		this.classroomMapper = classroomMapper;
	}

	@Override
	public ClassroomDto createClassroom(ClassroomDto classroom) {
		Classroom returnedClassroom = classroomRepository.save(
				classroomMapper.classroomDtoToClassroom(classroom));
		return classroomMapper.classroomToClassroomDto(returnedClassroom);
	}

	@Override
	public void removeClassroom(int idClassroom) {
		Optional<Classroom> foundClassroom = classroomRepository.findById(idClassroom);
		if(foundClassroom.isEmpty()) {
			throw new NoSuchClassroomException("No such classroom or incorrect idClassroom");
		}
		classroomRepository.deleteById(idClassroom);
	}

	@Override
	public ClassroomDto updateClassroom(ClassroomDto classroom) {
		Optional<Classroom> foundClassroom = classroomRepository.findById(classroom.getIdClassroom());
		if(foundClassroom.isEmpty()) {
			throw new NoSuchClassroomException("No such classroom or incorrect idClassroom");
		}
		return createClassroom(classroom);
	}

}
