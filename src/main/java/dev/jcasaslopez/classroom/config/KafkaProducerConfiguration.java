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
	private final int numberReplicas = 3;

	
	@Bean
    NewTopic classroomsTopic() {
		logger.info("Configuring Kafka Topic: {} with {} partitions and {} replicas", topicName, numberPartitions, numberReplicas);
        return TopicBuilder.name(topicName)
                .partitions(numberPartitions)
                .replicas(numberReplicas) 
                // Use log compaction: keep only the latest snapshot per classroom ID; intermediate state changes are not required
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_COMPACT)
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "2")
                .build();
    }

}
