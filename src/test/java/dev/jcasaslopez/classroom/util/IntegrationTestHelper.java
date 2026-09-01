package dev.jcasaslopez.classroom.util;

import java.util.List;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import dev.jcasaslopez.classroom.dto.ClassroomRequestDto;
import dev.jcasaslopez.classroom.dto.ClassroomResponseDto;
import dev.jcasaslopez.classroom.shared.utility.StandardResponse;

public class IntegrationTestHelper {

    private static TestRestTemplate testRestTemplate;
    private static String token;

    public static void setRestTemplate(TestRestTemplate restTemplate) {
        testRestTemplate = restTemplate;
    }

    private static String fetchToken() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<StandardResponse<String>> response = testRestTemplate.exchange(
                ClassroomEndpoints.GENERATE_TOKEN,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<StandardResponse<String>>() {}
        );
        return response.getBody().details();
    }

    private static String getToken() {
        if (token == null) {
            token = fetchToken();
        }
        return token;
    }

    private static HttpHeaders setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getToken());
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

	public static ResponseEntity<StandardResponse<Void>> createClassroom(ClassroomRequestDto classroom) {
	    HttpEntity<ClassroomRequestDto> requestEntity = new HttpEntity<>(classroom, setHeaders());
	    return testRestTemplate.exchange(
	            ClassroomEndpoints.CLASSROOMS,
	            HttpMethod.POST,
	            requestEntity,
	            new ParameterizedTypeReference<StandardResponse<Void>>() {}
	    );
	}
	
	public static ResponseEntity<StandardResponse<Void>> deleteClassroom(int idClassroom) {		
	    HttpEntity<Void> requestEntity = new HttpEntity<>(setHeaders());
	    return testRestTemplate.exchange(
	    		ClassroomEndpoints.CLASSROOM_BY_ID,
	            HttpMethod.DELETE,
	            requestEntity,
	            new ParameterizedTypeReference<StandardResponse<Void>>() {},
	            idClassroom
	    );
	}
	
	public static ResponseEntity<StandardResponse<Void>> updateClassroom(ClassroomRequestDto classroom, int idClassroom) {
	    HttpEntity<ClassroomRequestDto> requestEntity = new HttpEntity<>(classroom, setHeaders());
	    return testRestTemplate.exchange(
	            ClassroomEndpoints.CLASSROOM_BY_ID,
	            HttpMethod.PUT,
	            requestEntity,
	            new ParameterizedTypeReference<StandardResponse<Void>>() {},
	            idClassroom      
	    );
	}
	
	public static ResponseEntity<StandardResponse<List<ClassroomResponseDto>>> getClassroomList() {
	    HttpEntity<Void> requestEntity = new HttpEntity<>(setHeaders());
	    return testRestTemplate.exchange(
	            ClassroomEndpoints.CLASSROOMS,
	            HttpMethod.GET,
	            requestEntity,
	            new ParameterizedTypeReference<StandardResponse<List<ClassroomResponseDto>>>() {}	    
	            );
	}

}
