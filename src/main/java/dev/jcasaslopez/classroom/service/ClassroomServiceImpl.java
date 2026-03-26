package dev.jcasaslopez.classroom.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.jcasaslopez.classroom.dto.ClassroomDto;
import dev.jcasaslopez.classroom.entity.Classroom;
import dev.jcasaslopez.classroom.exception.NoSuchClassroomException;
import dev.jcasaslopez.classroom.mapper.ClassroomMapper;
import dev.jcasaslopez.classroom.producer.ClassroomEventProducer;
import dev.jcasaslopez.classroom.repository.ClassroomRepository;

@Service
public class ClassroomServiceImpl implements ClassroomService {
	
	private static final Logger logger = LoggerFactory.getLogger(ClassroomServiceImpl.class);
	
	private final ClassroomRepository classroomRepository;
	private final ClassroomMapper classroomMapper;
	private final ClassroomEventProducer producer;
	
	public ClassroomServiceImpl(ClassroomRepository classroomRepository, ClassroomMapper classroomMapper,
			ClassroomEventProducer producer) {
		this.classroomRepository = classroomRepository;
		this.classroomMapper = classroomMapper;
		this.producer = producer;
	}

	@Override
	public ClassroomDto createClassroom(ClassroomDto classroom) {
		Classroom returnedClassroom = classroomRepository.save(classroomMapper.classroomDtoToClassroom(classroom));
		logger.info("Classroom created successfully: Name= {}, ID= {}", returnedClassroom.getName(), returnedClassroom.getIdClassroom());
		return classroomMapper.classroomToClassroomDto(returnedClassroom);
	}

	@Override
	public void deleteClassroom(int idClassroom) {
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
		Optional<Classroom> foundClassroom = classroomRepository.findById(classroom.getIdClassroom());
		if(foundClassroom.isEmpty()) {
			logger.warn("Cannot update, classroom not found with ID: {}", classroom.getIdClassroom());
			throw new NoSuchClassroomException("No such classroom or incorrect idClassroom");
		}
		Classroom updatedClassroom = classroomRepository.save(classroomMapper.classroomDtoToClassroom(classroom));
		logger.info("Classroom updated successfully: Name= {}, ID= {}", updatedClassroom.getName(), updatedClassroom.getIdClassroom());
		return classroomMapper.classroomToClassroomDto(updatedClassroom);
	}

	@Override
	public List<ClassroomDto> findAll() {
		List<Classroom> allClassrooms = classroomRepository.findAll();
		logger.debug("Found {} classrooms", allClassrooms.size()); 
		return allClassrooms.stream()
					.map(c -> classroomMapper.classroomToClassroomDto(c))
					.toList();
	}
	
	@Override
	// This method is triggered by CommandLineRunner upon microservice startup to ensure the initial state is synchronized with Kafka.
	public void publishAllClassrooms() {
		findAll().forEach(
				classroom -> {
					producer.publishClassroom(classroomMapper.classroomDtoToClassroom(classroom));
				}
				);	
	}

}