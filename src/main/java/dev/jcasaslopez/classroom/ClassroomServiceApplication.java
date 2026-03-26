package dev.jcasaslopez.classroom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

import dev.jcasaslopez.classroom.producer.ClassroomEventProducer;

@SpringBootApplication
public class ClassroomServiceApplication {
	
	@Autowired ClassroomEventProducer classroomEventProducer;
	
	private static final Logger logger = LoggerFactory.getLogger(ClassroomServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ClassroomServiceApplication.class, args);
	}

    @Bean
    RestClient getClient() {
		return RestClient.create();
	}
    
    // The CommandLineRunner interface calls classroomEventProducer.publishClassrooms() to publish the updated list of
    // classrooms, read straight from the database.  
    @Bean
    CommandLineRunner init() {
        return args -> {
            logger.info("Starting initial classroom synchronization...");
            try {
                classroomEventProducer.publishAllClassrooms();
                logger.info("Initial classroom synchronization completed successfully.");
            } catch (Exception ex) {
                logger.error("Failed to perform initial synchronization", ex);
            }
        };
    }

}
