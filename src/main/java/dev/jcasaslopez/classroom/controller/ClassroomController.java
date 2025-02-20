package dev.jcasaslopez.classroom.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

	@PostMapping(value="/createClassroom", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StandardResponse> createClassroom(@Valid @RequestBody ClassroomDto classroom){
		classroomService.createClassroom(classroom);
		StandardResponse response = new StandardResponse (LocalDateTime.now(), 
				"Classroom created successfully", null, HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@DeleteMapping(value="/deleteClassroom")
	public ResponseEntity<StandardResponse> deleteClassroom(@RequestParam int idClassroom){
		classroomService.deleteClassroom(idClassroom);
		StandardResponse response = new StandardResponse (LocalDateTime.now(), 
				"Classroom deleted successfully", null, HttpStatus.OK);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PutMapping(value="/updateClassroom", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StandardResponse> updateClassroom(@Valid @RequestBody ClassroomDto classroom){
		classroomService.updateClassroom(classroom);
		StandardResponse response = new StandardResponse (LocalDateTime.now(), 
				"Classroom updated successfully", null, HttpStatus.OK);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
}
