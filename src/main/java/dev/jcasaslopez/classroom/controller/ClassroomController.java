package dev.jcasaslopez.classroom.controller;

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
import dev.jcasaslopez.classroom.service.ClassroomService;
import dev.jcasaslopez.classroom.shared.utility.StandardResponse;
import jakarta.validation.Valid;

@CrossOrigin(origins = {"${frontend.url}"})
@RestController
public class ClassroomController {
	
	private final ClassroomService classroomService;
	
	public ClassroomController(ClassroomService classroomService) {
		this.classroomService = classroomService;
	}

	@PostMapping(value="/createClassroom", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StandardResponse<Void>> createClassroom(@Valid @RequestBody ClassroomDto classroom){
		classroomService.createClassroom(classroom);
		StandardResponse<Void> response = new StandardResponse<> ("Classroom created successfully", null, HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@DeleteMapping(value="/deleteClassroom")
	public ResponseEntity<StandardResponse<Void>> deleteClassroom(@RequestParam int idClassroom){
		classroomService.deleteClassroom(idClassroom);
		StandardResponse<Void> response = new StandardResponse<> ("Classroom deleted successfully", null, HttpStatus.OK);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PutMapping(value="/updateClassroom", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StandardResponse<Void>> updateClassroom(@Valid @RequestBody ClassroomDto classroom){
		classroomService.updateClassroom(classroom);
		StandardResponse<Void> response = new StandardResponse<> ("Classroom updated successfully", null, HttpStatus.OK);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
}
