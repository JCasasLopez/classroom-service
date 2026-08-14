package dev.jcasaslopez.classroom.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import dev.jcasaslopez.classroom.mapper.ClassroomMapper;
import dev.jcasaslopez.classroom.repository.ClassroomRepository;
import dev.jcasaslopez.classroom.shared.event.ClassroomEvent;

@Service
public class ClassroomEventProducerImpl implements ClassroomEventProducer {

    @Value("${spring.kafka.producer.topic-name}")
    private String topicName;
    private static final Logger logger = LoggerFactory.getLogger(ClassroomEventProducerImpl.class);
    private final ClassroomRepository classroomRepository;
    private final ClassroomMapper mapper;
    private final KafkaTemplate<String, ClassroomEvent> kafkaTemplate;

    public ClassroomEventProducerImpl(ClassroomRepository classroomRepository,
            ClassroomMapper mapper, KafkaTemplate<String, ClassroomEvent> kafkaTemplate) {
        this.classroomRepository = classroomRepository;
        this.mapper = mapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    // This method is triggered by ClassroomStartupHandler upon microservice startup to ensure the initial state is 
    // synchronized with Kafka.
    @Override
    public void publishAllClassrooms() {
    	classroomRepository.findAll().forEach(
    			classroom -> publishClassroom(mapper.entityToClassroomEvent(classroom))
    			);
    }
	
	@Override
	public void publishClassroom(ClassroomEvent classroom) {
		try {
			kafkaTemplate.send(topicName, String.valueOf(classroom.getIdClassroom()), classroom).join();
			logger.info("Publish Classroom {}", classroom.getIdClassroom());
		} catch (Exception ex) {
		    throw new RuntimeException("Error sending Kafka message to topic: " + topicName, ex);
		}
	}
	
	// Sends a Tombstone (null payload) to trigger log compaction and notify consumers to delete the record locally.
	@Override
	public void sendTombstone(int idClassroom) {
		try {
			kafkaTemplate.send(topicName, String.valueOf(idClassroom), null).join();
			logger.info("Sending tombstone for Classroom {}", idClassroom);
		} catch (Exception ex) {
		    throw new RuntimeException("Error sending Kafka message to topic: " + topicName, ex);
		}
		
	}

}