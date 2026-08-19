package dev.jcasaslopez.classroom.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaProducerConfiguration {
	
	@Value("${spring.kafka.producer.topic-name}") private String topicName;
	@Value("${spring.kafka.producer.topic-number-replicas}") private  int numberReplicas;
	@Value("${spring.kafka.producer.topic-number-in-sync-replicas}") private  int numberInSyncReplicas;
	
	// Each message takes up roughly 160 bytes
	private static final int SEGMENT_SIZE = 1_048_576;
	// This is constant. As classrooms volume will be low and there is no need for parallel processing, can be set to 1.
	private static final int NUMBER_PARTITIONS = 1;
	private static final double DIRTY_RATIO = 0.1;

	private static final Logger logger = LoggerFactory.getLogger(KafkaProducerConfiguration.class);
		
	@Bean
    NewTopic classroomsTopic() {
		logger.info("Configuring Kafka Topic: {} with {} partitions and {} replicas", topicName, NUMBER_PARTITIONS, numberReplicas);
        return TopicBuilder.name(topicName)
                .partitions(NUMBER_PARTITIONS)
                .replicas(numberReplicas) 
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_COMPACT)                
                .config(TopicConfig.SEGMENT_BYTES_CONFIG, String.valueOf(SEGMENT_SIZE))   
                .config(TopicConfig.MIN_CLEANABLE_DIRTY_RATIO_CONFIG, String.valueOf(DIRTY_RATIO))
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, String.valueOf(numberInSyncReplicas))
                .build();
    }

}
