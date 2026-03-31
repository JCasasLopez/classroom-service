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
	
	@Value("${spring.kafka.producer.topic-name}")
	private String topicName;
	
	private static final Logger logger = LoggerFactory.getLogger(KafkaProducerConfiguration.class);
	private final int numberPartitions = 1;
	private final int numberReplicas = 1;

	
	@Bean
    NewTopic classroomsTopic() {
		logger.info("Configuring Kafka Topic: {} with {} partitions and {} replicas", topicName, numberPartitions, numberReplicas);
        return TopicBuilder.name(topicName)
                .partitions(numberPartitions)
                .replicas(numberReplicas) 
                
                // Use log compaction: keep only the latest snapshot per classroom ID; intermediate state changes are not required
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_COMPACT)
                
                // Max size of a single segment before Kafka starts a new one. Remember that Kafka does not compact a segment
                // until has been close.
                // 50KB which is enough for roughly 256 messages (Classroom JSON as payload).
                .config(TopicConfig.SEGMENT_BYTES_CONFIG, "51200") 
                
                // Percentage of "updated/old" data allowed before Kafka starts the cleaning process.
                .config(TopicConfig.MIN_CLEANABLE_DIRTY_RATIO_CONFIG, "0.1")
                
                // How long Kafka keeps the deleted messages so consumers can see them.
                .config(TopicConfig.DELETE_RETENTION_MS_CONFIG, "86400000")
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "1")
                .build();
    }

}
