package dev.jcasaslopez.classroom.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.jcasaslopez.classroom.dto.ClassroomRequestDto;
import dev.jcasaslopez.classroom.dto.ClassroomResponseDto;
import dev.jcasaslopez.classroom.service.ClassroomService;
import dev.jcasaslopez.classroom.shared.utility.StandardResponse;
import dev.jcasaslopez.classroom.util.ClassroomEndpoints;
import jakarta.validation.Valid;

@CrossOrigin(origins = {"${frontend.url}"})
@RestController
public class ClassroomController {

    private final ClassroomService classroomService;

    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @PostMapping(value = ClassroomEndpoints.CLASSROOMS, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<Void>> createClassroom(@Valid @RequestBody ClassroomRequestDto classroom) {
        classroomService.createClassroom(classroom);
        StandardResponse<Void> response = new StandardResponse<>("Classroom created successfully", null, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = ClassroomEndpoints.CLASSROOM_BY_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<Void>> updateClassroom(@PathVariable int id, @Valid @RequestBody ClassroomRequestDto classroom) {
        classroomService.updateClassroom(id, classroom);
        StandardResponse<Void> response = new StandardResponse<>("Classroom updated successfully", null, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(ClassroomEndpoints.CLASSROOM_BY_ID)
    public ResponseEntity<StandardResponse<Void>> deleteClassroom(@PathVariable int id) {
        classroomService.deleteClassroom(id);
        StandardResponse<Void> response = new StandardResponse<>("Classroom deleted successfully", null, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping(ClassroomEndpoints.CLASSROOMS)
    public ResponseEntity<StandardResponse<List<ClassroomResponseDto>>> getClassroomsList() {
        StandardResponse<List<ClassroomResponseDto>> response = new StandardResponse<>(
        					"Classrooms list retrieved successfully", classroomService.findAll(), HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}