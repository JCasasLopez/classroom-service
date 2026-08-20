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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin(origins = {"${frontend.url}"})
@RestController
@Tag(name = "Classroom Controller", description = "Endpoints for managing classrooms")
public class ClassroomController {

    private final ClassroomService classroomService;

    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @Operation(summary = "Create a classroom", description = "Creates a new classroom in the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Classroom created successfully",
            content = @Content(schema = @Schema(implementation = StandardResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid or missing input fields",
            content = @Content(schema = @Schema(implementation = StandardResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized – missing or invalid access token",
			content = @Content(schema = @Schema(implementation = StandardResponse.class))),
        @ApiResponse(responseCode = "403", description = "Access denied (User is not an admin)",
            content = @Content(schema = @Schema(implementation = StandardResponse.class))),
        @ApiResponse(responseCode = "409", description = "Classroom with this name already exists",
            content = @Content(schema = @Schema(implementation = StandardResponse.class)))
    })
    @PostMapping(value = ClassroomEndpoints.CLASSROOMS, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<Void>> createClassroom(@Valid @RequestBody ClassroomRequestDto classroom) {
        classroomService.createClassroom(classroom);
        StandardResponse<Void> response = new StandardResponse<>("Classroom created successfully", null, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update a classroom", description = "Updates an existing classroom details by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Classroom updated successfully",
                content = @Content(schema = @Schema(implementation = StandardResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid or missing input fields",
                content = @Content(schema = @Schema(implementation = StandardResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized – missing or invalid access token",
    			content = @Content(schema = @Schema(implementation = StandardResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied (User is not an admin)",
                content = @Content(schema = @Schema(implementation = StandardResponse.class))),
            @ApiResponse(responseCode = "404", description = "Classroom not found",
            	content = @Content(schema = @Schema(implementation = StandardResponse.class))),
            @ApiResponse(responseCode = "409", description = "Classroom with this name already exists",
                content = @Content(schema = @Schema(implementation = StandardResponse.class)))
        })
    @PutMapping(value = ClassroomEndpoints.CLASSROOM_BY_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<Void>> updateClassroom(@Parameter(description = "ID of the classroom to update", required = true, example = "1")
    @PathVariable int id, 
    @Valid @RequestBody ClassroomRequestDto classroom) {
        classroomService.updateClassroom(id, classroom);
        StandardResponse<Void> response = new StandardResponse<>("Classroom updated successfully", null, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Delete a classroom", description = "Deletes a classroom from the database by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Classroom deleted successfully",
            content = @Content(schema = @Schema(implementation = StandardResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized – missing or invalid access token",
			content = @Content(schema = @Schema(implementation = StandardResponse.class))),
        @ApiResponse(responseCode = "403", description = "Access denied (User is not an admin)",
            content = @Content(schema = @Schema(implementation = StandardResponse.class))),
        @ApiResponse(responseCode = "404", description = "Classroom not found",
            content = @Content(schema = @Schema(implementation = StandardResponse.class)))
    })
    @DeleteMapping(ClassroomEndpoints.CLASSROOM_BY_ID)
    public ResponseEntity<StandardResponse<Void>> deleteClassroom(@Parameter(description = "ID of the classroom to delete", required = true, example = "1")
    @PathVariable int id) {
        classroomService.deleteClassroom(id);
        StandardResponse<Void> response = new StandardResponse<>("Classroom deleted successfully", null, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @Operation(summary = "Get classrooms list", description = "Retrieves a complete list of all registered classrooms.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List retrieved successfully",
            content = @Content(schema = @Schema(implementation = StandardResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized – missing or invalid access token",
			content = @Content(schema = @Schema(implementation = StandardResponse.class))),
        @ApiResponse(responseCode = "403", description = "Access denied (User is not an admin)",
        	content = @Content(schema = @Schema(implementation = StandardResponse.class))),
    })
    @GetMapping(ClassroomEndpoints.CLASSROOMS)
    public ResponseEntity<StandardResponse<List<ClassroomResponseDto>>> getClassroomsList() {
        StandardResponse<List<ClassroomResponseDto>> response = new StandardResponse<>(
        					"Classrooms list retrieved successfully", classroomService.findAll(), HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}