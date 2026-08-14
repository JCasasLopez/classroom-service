package dev.jcasaslopez.classroom.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import dev.jcasaslopez.classroom.shared.event.ClassroomEvent;

@Component
public class ClassroomEventProducer {
	
	@Value("${spring.kafka.producer.topic-name}")
	private String topicName;
	private static final Logger logger = LoggerFactory.getLogger(ClassroomEventProducer.class);
	private final KafkaTemplate<String, ClassroomEvent> kafkaTemplate;

	public ClassroomEventProducer(KafkaTemplate<String, ClassroomEvent> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
	
	public void publishClassroom(ClassroomEvent classroom) {
		try {
			kafkaTemplate.send(topicName, String.valueOf(classroom.getIdClassroom()), classroom).join();
			logger.info("Publish Classroom {}", classroom.getIdClassroom());
		} catch (Exception ex) {
		    throw new RuntimeException("Error sending Kafka message to topic: " + topicName, ex);
		}
	}
	
	// Sends a Tombstone (null payload) to trigger log compaction and notify consumers to delete the record locally.
	public void sendTombstone(int idClassroom) {
		try {
			kafkaTemplate.send(topicName, String.valueOf(idClassroom), null).join();
			logger.info("Sending tombstone for Classroom {}", idClassroom);
		} catch (Exception ex) {
		    throw new RuntimeException("Error sending Kafka message to topic: " + topicName, ex);
		}
		
	}

}