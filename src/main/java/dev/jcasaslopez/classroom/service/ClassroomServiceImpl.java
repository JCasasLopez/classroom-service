package dev.jcasaslopez.classroom.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.jcasaslopez.classroom.dto.ClassroomRequestDto;
import dev.jcasaslopez.classroom.dto.ClassroomResponseDto;
import dev.jcasaslopez.classroom.entity.Classroom;
import dev.jcasaslopez.classroom.exception.NoSuchClassroomException;
import dev.jcasaslopez.classroom.mapper.ClassroomMapper;
import dev.jcasaslopez.classroom.producer.ClassroomEventProducer;
import dev.jcasaslopez.classroom.repository.ClassroomRepository;

@Service
public class ClassroomServiceImpl implements ClassroomService {
	
	private static final Logger logger = LoggerFactory.getLogger(ClassroomServiceImpl.class);
	
	private final ClassroomRepository classroomRepository;
	private final ClassroomMapper mapper;
	private final ClassroomEventProducer producer;
	
	public ClassroomServiceImpl(ClassroomRepository classroomRepository, ClassroomMapper mapper,
			ClassroomEventProducer producer) {
		this.classroomRepository = classroomRepository;
		this.mapper = mapper;
		this.producer = producer;
	}

	@Override
	public ClassroomResponseDto createClassroom(ClassroomRequestDto classroom) {
		Classroom returnedClassroom = classroomRepository.save(mapper.requestToEntity(classroom));
		logger.info("Classroom created successfully: Name= {}, ID= {}", returnedClassroom.getName(), returnedClassroom.getIdClassroom());
		producer.publishClassroom(mapper.entityToClassroomEvent(returnedClassroom));
		return mapper.entityToResponse(returnedClassroom);
	}

	@Override
	public void deleteClassroom(int idClassroom) {
		Optional<Classroom> foundClassroom = classroomRepository.findById(idClassroom);
		if(foundClassroom.isEmpty()) {
            logger.warn("Classroom not found with ID: {}", idClassroom);
			throw new NoSuchClassroomException("Classroom not found in the database");
		}
		classroomRepository.deleteById(idClassroom);
        logger.info("Classroom deleted successfully with ID: {}", idClassroom);
		producer.sendTombstone(idClassroom);
	}

	@Override
	public ClassroomResponseDto updateClassroom(int idClassroom, ClassroomRequestDto classroom) {
		classroomRepository.findById(idClassroom)
		        .orElseThrow(() -> {
		            logger.warn("Cannot update, classroom not found with ID: {}", idClassroom);
		            return new NoSuchClassroomException("Classroom not found in the database");
		        });
		
		Classroom classroomToUpdate = mapper.requestToEntity(classroom);
		classroomToUpdate.setIdClassroom(idClassroom);
		Classroom updatedClassroom = classroomRepository.save(classroomToUpdate);

		logger.info("Classroom updated successfully: Name= {}, ID= {}", updatedClassroom.getName(), updatedClassroom.getIdClassroom());
		producer.publishClassroom(mapper.entityToClassroomEvent(updatedClassroom));
		return mapper.entityToResponse(updatedClassroom);
	}

	@Override
	public List<ClassroomResponseDto> findAll() {
		List<Classroom> allClassrooms = classroomRepository.findAll();
		logger.debug("Found {} classrooms", allClassrooms.size()); 
		return allClassrooms.stream()
					.map(c -> mapper.entityToResponse(c))
					.toList();
	}
	
	@Override
	// This method is triggered by ClassroomStartupHandler upon microservice startup to ensure the initial state is 
	// synchronized with Kafka.
	public void publishAllClassrooms() {
		findAll().forEach(
				classroom -> {
					producer.publishClassroom(mapper.responseToClassroomEvent(classroom));
				}
				);	
	}

}