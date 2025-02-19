package dev.jcasaslopez.classroom.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.jcasaslopez.classroom.dto.ClassroomDto;
import dev.jcasaslopez.classroom.dto.StandardResponse;
import dev.jcasaslopez.classroom.service.ClassroomService;
import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
public class ClassroomController {
	
	private final ClassroomService classroomService;
	
	public ClassroomController(ClassroomService classroomService) {
		this.classroomService = classroomService;
	}

	@PostMapping(value="/createClassroom")
	public ResponseEntity<StandardResponse> createClassroom(@Valid @RequestBody ClassroomDto classroom){
		classroomService.createClassroom(classroom);
		StandardResponse response = new StandardResponse (LocalDateTime.now(), 
				"Classroom created successfully", null, HttpStatus.OK);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
