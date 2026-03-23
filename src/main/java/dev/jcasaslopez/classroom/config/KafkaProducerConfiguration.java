package dev.jcasaslopez.classroom.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaProducerConfiguration {
	
	@Bean
    public NewTopic classroomsTopic() {
        return TopicBuilder.name("classrooms-list")
                .partitions(1)
                .replicas(3) 
                // Use log compaction: keep only the latest snapshot per classroom ID; intermediate state changes are not required
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_COMPACT)
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "2")
                .build();
    }

}
