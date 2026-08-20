package dev.jcasaslopez.classroom.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jcasaslopez.classroom.shared.security.GenerateJwt;
import dev.jcasaslopez.classroom.shared.utility.StandardResponse;
import dev.jcasaslopez.classroom.util.ClassroomEndpoints;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = {"${frontend.url}"})
@RestController
@Tag(name = "Auth (Testing utility)", description = "Development/testing helper for generating JWTs to explore the API with tools like Postman")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	@Value("${jwt.secretKey}") private String secretKey;

	@Operation(
			summary = "[Testing only] Generates a valid JWT for a given user id",
			description = """
					Convenience endpoint for exploring the API with tools like Postman, without going through the full login flow.
					Generates a valid access token for the given `idUser`, defaulting to user id 1 if not specified.

					This endpoint bypasses real authentication and should never be exposed in a production environment.
					"""
			)
	@ApiResponse(responseCode = "200", description = "JWT generated successfully",
	content = @Content(schema = @Schema(implementation = StandardResponse.class)))
	@GetMapping(value = ClassroomEndpoints.GENERATE_TOKEN)
	public ResponseEntity<StandardResponse<String>> generateToken() {
		logger.debug("GET /generate-token");

		String message = String.format("JWT created successfully");
		String jwt = new GenerateJwt(secretKey).withRoleAdmin().build();
		StandardResponse<String> response = new StandardResponse<>(message, jwt, HttpStatus.OK);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
