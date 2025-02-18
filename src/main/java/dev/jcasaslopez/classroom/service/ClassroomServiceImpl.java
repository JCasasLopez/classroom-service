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

	@Override
	public boolean isUserAdmin(String[] roles) {
		
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
		
		if(roles.length > 1) {
			return true;
		}
		return false;
	}
	
}
