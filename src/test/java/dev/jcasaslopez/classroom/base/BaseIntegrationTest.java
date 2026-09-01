package dev.jcasaslopez.classroom.base;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.jcasaslopez.classroom.repository.ClassroomRepository;
import dev.jcasaslopez.classroom.util.IntegrationTestHelper;
import jakarta.annotation.PostConstruct;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected ClassroomRepository repository;
    @Autowired private TestRestTemplate testRestTemplate;
    
    @ServiceConnection
    static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.3");
    protected static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("apache/kafka"));

    @PostConstruct
    void initHelper() {
        IntegrationTestHelper.setRestTemplate(testRestTemplate);
    }
    
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
}