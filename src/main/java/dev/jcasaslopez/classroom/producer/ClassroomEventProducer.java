package dev.jcasaslopez.classroom.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import dev.jcasaslopez.classroom.event.ClassroomEvent;
import dev.jcasaslopez.classroom.mapper.ClassroomMapper;
import dev.jcasaslopez.classroom.service.ClassroomService;

@Component
public class ClassroomEventProducer {
	
	@Value("${spring.kafka.producer.topic-name}")
	private String topicName;
	
	private final KafkaTemplate<String, ClassroomEvent> kafkaTemplate;
	private final ClassroomService classroomService;
	private final ClassroomMapper classroomMapper;

	public ClassroomEventProducer(KafkaTemplate<String, ClassroomEvent> kafkaTemplate,
			ClassroomService classroomService, ClassroomMapper classroomMapper) {
		this.kafkaTemplate = kafkaTemplate;
		this.classroomService = classroomService;
		this.classroomMapper = classroomMapper;
	}

	public void publishClassrooms() {
		classroomService.findAll().forEach(
				classroom -> {
					ClassroomEvent classroomEvent = classroomMapper.classroomDtoToClassroomEvent(classroom);
					kafkaTemplate.send(topicName, String.valueOf(classroom.getIdClassroom()), classroomEvent);
				}
				);	
		
	}

}