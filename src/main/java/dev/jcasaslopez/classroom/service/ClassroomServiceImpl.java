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
	public void removeClassroom(int idClassroom) {
		logger.info("Attempting to remove classroom with ID: {}", idClassroom);
		Optional<Classroom> foundClassroom = classroomRepository.findById(idClassroom);
		if(foundClassroom.isEmpty()) {
            logger.warn("Classroom not found with ID: {}", idClassroom);
			throw new NoSuchClassroomException("No such classroom or incorrect idClassroom");
		}
		classroomRepository.deleteById(idClassroom);
        logger.info("Classroom removed successfully with ID: {}", idClassroom);
	}

	@Override
	public ClassroomDto updateClassroom(ClassroomDto classroom) {
        logger.info("Updating classroom with ID: {}", classroom.getIdClassroom());
		Optional<Classroom> foundClassroom = classroomRepository.findById(classroom.getIdClassroom());
		if(foundClassroom.isEmpty()) {
            logger.warn("Cannot update, classroom not found with ID: {}", classroom.getIdClassroom());
			throw new NoSuchClassroomException("No such classroom or incorrect idClassroom");
		}
		ClassroomDto updatedClassroom = createClassroom(classroom);
        logger.info("Classroom updated successfully: Name= {}, ID= {}", updatedClassroom.getName(),
        		updatedClassroom.getIdClassroom());
        return updatedClassroom;
	}

	@Override
	public boolean isUserAdmin(String username, String[] roles) {
		
		/* Un filtro captura el token JWT usado para la autenticación de los usuarios a partir del 
		   encabezado de la petición HTTP, extrae sus "claims" y al llamar a este método, manda el 
		   array con los roles como parámetro.

		   Por defecto, cuando se crea un nuevo usuario, se le asigna automáticamente el rol "user";
		   si después se promociona a ese usuario a "admin", ambos roles se mantienen en su atributo
		   "roles"; por tanto, si dicho array tiene un tamaño superior a 1, sabemos con certeza que
		   ese usuario posee el estatus de "admin". 

		   ---------------------------------------------------------------------------------------

		   A filter captures the token JWT used for authentication from the HTTP request header,
		   extracts its claims and sends the roles in the form of an array as a parameter when 
		   calling this method.

		   By default, a new user is assigned the role "user" when first created; if later 
		   the user is upgraded to "admin", both roles are kept, hence if the array's length is 
		   higher than 1, we know for certain that the user is an "admin". */
		
		logger.debug("Checking if user {} is admin", username);

		boolean isAdmin = roles.length > 1;
        if (isAdmin) {
            logger.info("User {} is an admin.", username);
        } else {
            logger.info("User {} is not an admin.", username);
        }
        return isAdmin;
	}
	
}
