package dev.jcasaslopez.classroom.service;

import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import dev.jcasaslopez.classroom.dto.ClassroomDto;
import dev.jcasaslopez.classroom.entity.Classroom;
import dev.jcasaslopez.classroom.exception.NoSuchClassroomException;
import dev.jcasaslopez.classroom.mapper.ClassroomMapper;
import dev.jcasaslopez.classroom.repository.ClassroomRepository;

@Service
public class ClassroomServiceImpl implements ClassroomService {
	
	private static final Logger logger = LoggerFactory.getLogger(ClassroomServiceImpl.class);
	
	private final ClassroomRepository classroomRepository;
	private final ClassroomMapper classroomMapper;
	
	public ClassroomServiceImpl(ClassroomRepository classroomRepository, ClassroomMapper classroomMapper) {
		this.classroomRepository = classroomRepository;
		this.classroomMapper = classroomMapper;
	}

	@Override
	public ClassroomDto createClassroom(ClassroomDto classroom) {
		logger.info("Creating new classroom: Name= {}, ID= {}", classroom.getName(), classroom.getIdClassroom());
		Classroom returnedClassroom = classroomRepository.save(
				classroomMapper.classroomDtoToClassroom(classroom));
		logger.info("Classroom created successfully: Name= {}, ID= {}", returnedClassroom.getName(), 
				returnedClassroom.getIdClassroom());
		return classroomMapper.classroomToClassroomDto(returnedClassroom);
	}

	@Override
	public void deleteClassroom(int idClassroom) {
		logger.info("Attempting to delete classroom with ID: {}", idClassroom);
		Optional<Classroom> foundClassroom = classroomRepository.findById(idClassroom);
		if(foundClassroom.isEmpty()) {
            logger.warn("Classroom not found with ID: {}", idClassroom);
			throw new NoSuchClassroomException("No such classroom or incorrect idClassroom");
		}
		classroomRepository.deleteById(idClassroom);
        logger.info("Classroom deleted successfully with ID: {}", idClassroom);
	}

	@Override
	public ClassroomDto updateClassroom(ClassroomDto classroom) {
		logger.info("Updating classroom with ID: {}", classroom.getIdClassroom());
		Optional<Classroom> foundClassroom = classroomRepository.findById(classroom.getIdClassroom());
		if(foundClassroom.isEmpty()) {
			logger.warn("Cannot update, classroom not found with ID: {}", classroom.getIdClassroom());
			throw new NoSuchClassroomException("No such classroom or incorrect idClassroom");
		}
		Classroom updatedClassroom = classroomRepository.save(
				classroomMapper.classroomDtoToClassroom(classroom));
		logger.info("Classroom updated successfully: Name= {}, ID= {}", updatedClassroom.getName(),
				updatedClassroom.getIdClassroom());
		return classroomMapper.classroomToClassroomDto(updatedClassroom);
	}

}
