package dev.jcasaslopez.classroom.base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.jcasaslopez.classroom.repository.ClassroomRepository;
import dev.jcasaslopez.classroom.shared.utility.StandardResponse;
import dev.jcasaslopez.classroom.util.ClassroomEndpoints;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired protected TestRestTemplate testRestTemplate;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected ClassroomRepository repository;
    
    @Value("${jwt.secretKey}") private String secretKey;
    protected static String token;

    @ServiceConnection
    static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.3");
    protected static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("apache/kafka"));

    static {
        mySQLContainer.start();
        kafkaContainer.start();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }
    
    @AfterEach
    void cleanDatabase() {
        repository.deleteAllInBatch();
    }
    
    @BeforeAll
    static void obtainTokenOnce(@Autowired TestRestTemplate testRestTemplate) {
        if (token == null) {
            token = fetchToken(testRestTemplate);
        }
    }

    private static String fetchToken(TestRestTemplate testRestTemplate) {
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
}