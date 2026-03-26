package dev.jcasaslopez.classroom.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import dev.jcasaslopez.classroom.entity.Classroom;
import dev.jcasaslopez.classroom.event.ClassroomEvent;
import dev.jcasaslopez.classroom.mapper.ClassroomMapper;

@Component
public class ClassroomEventProducer {
	
	@Value("${spring.kafka.producer.topic-name}")
	private String topicName;
	
	private static final Logger logger = LoggerFactory.getLogger(ClassroomEventProducer.class);
	
	private final KafkaTemplate<String, ClassroomEvent> kafkaTemplate;
	private final ClassroomMapper classroomMapper;

	public ClassroomEventProducer(KafkaTemplate<String, ClassroomEvent> kafkaTemplate, ClassroomMapper classroomMapper) {
		this.kafkaTemplate = kafkaTemplate;
		this.classroomMapper = classroomMapper;
	}
	
	public void publishClassroom (Classroom classroom) {
		ClassroomEvent classroomEvent = classroomMapper.classroomToClassroomEvent(classroom);
		kafkaTemplate.send(topicName, String.valueOf(classroom.getIdClassroom()), classroomEvent);
		logger.info("Publish Classroom {}", classroom.getIdClassroom());
	}

}