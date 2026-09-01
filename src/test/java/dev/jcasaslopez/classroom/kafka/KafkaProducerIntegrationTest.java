package dev.jcasaslopez.classroom.kafka;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import dev.jcasaslopez.classroom.base.BaseIntegrationTest;
import dev.jcasaslopez.classroom.dto.ClassroomRequestDto;
import dev.jcasaslopez.classroom.entity.Classroom;
import dev.jcasaslopez.classroom.shared.event.ClassroomEvent;
import dev.jcasaslopez.classroom.util.IntegrationTestHelper;
import dev.jcasaslopez.classroom.util.KafkaTestHelper;

public class KafkaProducerIntegrationTest extends BaseIntegrationTest {
	
	@Value("${spring.kafka.producer.topic-name}") private String topicName;
	
	private static final ClassroomRequestDto CLASSROOM = new ClassroomRequestDto("101", 50, true, true);
	private static final ClassroomRequestDto CLASSROOM2 = new ClassroomRequestDto("Isaac Newton", 70, true, true);
	private KafkaConsumer<String, ClassroomEvent> consumer;
	
	@Test
	void create_classroom_publishes_classroom_event_in_Kafka_topic() {
		// Arrange
		consumer = KafkaTestHelper.createClassroomsConsumer(kafkaContainer.getBootstrapServers(), topicName);
		IntegrationTestHelper.createClassroom(CLASSROOM);
		
		// Act
        List<ConsumerRecord<String, ClassroomEvent>> records = KafkaTestHelper.pollRecords(consumer, Duration.ofSeconds(10));
	
        // Assert
        ClassroomEvent classroom = records.get(0).value();
        
        assertAll(
        		() -> assertEquals(CLASSROOM.name(), classroom.getName()),
        		() -> assertEquals(CLASSROOM.seats(), classroom.getSeats()),
        		() -> assertEquals(CLASSROOM.projector(), classroom.getProjector()),
        		() -> assertEquals(CLASSROOM.speakers(), classroom.getSpeakers())
        		);
	}
	
	@Test
	void delete_classroom_deletes_classroom_event_in_Kafka_topic() {
		// Arrange
		consumer = KafkaTestHelper.createClassroomsConsumer(kafkaContainer.getBootstrapServers(), topicName);
		IntegrationTestHelper.createClassroom(CLASSROOM);
	
		// Act
        Classroom savedClassroom = repository.findAll().get(0);
        IntegrationTestHelper.deleteClassroom(savedClassroom.getIdClassroom());
        
        List<ConsumerRecord<String, ClassroomEvent>> records = KafkaTestHelper.pollRecords(consumer, Duration.ofSeconds(10));
	
        // Assert
        // The Tombstone with a null value has been published
        assertNull(records.get(1).value());
	}
	
	@Test
	void update_classroom_publishes_classroom_event_in_Kafka_topic() {
		// Arrange	
		consumer = KafkaTestHelper.createClassroomsConsumer(kafkaContainer.getBootstrapServers(), topicName);
		IntegrationTestHelper.createClassroom(CLASSROOM);
	
		// Act
        Classroom savedClassroom = repository.findAll().get(0);
        IntegrationTestHelper.updateClassroom(CLASSROOM2, savedClassroom.getIdClassroom());
        
        List<ConsumerRecord<String, ClassroomEvent>> records = KafkaTestHelper.pollRecords(consumer, Duration.ofSeconds(10));
	
        // Assert
        ClassroomEvent classroom = records.get(0).value();
        ClassroomEvent classroom2 = records.get(1).value();

        assertAll(
        		() -> assertEquals(CLASSROOM.name(), classroom.getName()),
        		() -> assertEquals(CLASSROOM2.name(), classroom2.getName())
        		);
	}

}