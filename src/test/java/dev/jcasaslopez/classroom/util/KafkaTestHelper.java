package dev.jcasaslopez.classroom.util;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import dev.jcasaslopez.classroom.shared.event.ClassroomEvent;

public final class KafkaTestHelper {
	public static KafkaConsumer<String, ClassroomEvent> createClassroomsConsumer(String bootstrapServers, String topic) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-classrooms-" + UUID.randomUUID());
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ClassroomEvent.class.getName());
		props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

		KafkaConsumer<String, ClassroomEvent> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(List.of(topic));
		
		// Partition assignment happens asynchronously after subscribe(), so we poll until it completes (or timeout) 
		// before we can seek. seekToEnd() only schedules the seek; calling position() forces it to resolve immediately,
		// so the consumer is positioned at the topic's current end, ignoring any events published by earlier tests.
		long deadline = System.currentTimeMillis() + 5000;
		while (consumer.assignment().isEmpty() && System.currentTimeMillis() < deadline) {
		    consumer.poll(Duration.ofMillis(100));
		}
		consumer.seekToEnd(consumer.assignment());
		consumer.assignment().forEach(consumer::position);
		return consumer;
		
	}
	
	public static List<ConsumerRecord<String, ClassroomEvent>> pollRecords(
	        KafkaConsumer<String, ClassroomEvent> consumer, Duration totalTimeout) {

	    List<ConsumerRecord<String, ClassroomEvent>> result = new ArrayList<>();
	    long deadline = System.currentTimeMillis() + totalTimeout.toMillis();

	    while (result.isEmpty() && System.currentTimeMillis() < deadline) {
	        ConsumerRecords<String, ClassroomEvent> records = consumer.poll(Duration.ofMillis(100));
	        records.forEach(result::add);
	    }

	    return result;
	}

}
