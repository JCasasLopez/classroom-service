package dev.jcasaslopez.classroom.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import dev.jcasaslopez.classroom.service.ClassroomService;

@Component
public class ClassroomStartupHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(ClassroomStartupHandler.class);
    private final ClassroomService classroomService;

    public ClassroomStartupHandler(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    // It triggers the service to publish the current state of classrooms (as it is in the database) to Kafka 
    // once the application context is fully operational (ApplicationReadyEvent).
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("Application is ready. Starting initial classroom synchronization...");
        try {
            classroomService.publishAllClassrooms();
            logger.info("Initial classroom synchronization completed successfully.");
        } catch (Exception ex) {
            logger.error("Failed to perform initial synchronization. Kafka might be unavailable.", ex);
        }
    }

}
