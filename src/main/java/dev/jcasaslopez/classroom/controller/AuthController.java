package dev.jcasaslopez.classroom.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.jcasaslopez.classroom.shared.security.GenerateJwt;
import dev.jcasaslopez.classroom.shared.utility.StandardResponse;
import dev.jcasaslopez.classroom.util.ClassroomEndpoints;

@CrossOrigin(origins = {"${frontend.url}"})
@RestController
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	@Value("${jwt.secretKey}") private String secretKey;

	
	@GetMapping(value = ClassroomEndpoints.GENERATE_TOKEN)
	public ResponseEntity<StandardResponse<String>> generateToken(@RequestParam(defaultValue = "1") int idUser) {
		logger.debug("GET /generate-token?idUser={}", idUser);

		String message = String.format("JWT created successfully for user ID %s", idUser);
		String jwt = new GenerateJwt(secretKey).withIdUser(idUser).withRoleAdmin().build();
		StandardResponse<String> response = new StandardResponse<>(message, jwt, HttpStatus.OK);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
