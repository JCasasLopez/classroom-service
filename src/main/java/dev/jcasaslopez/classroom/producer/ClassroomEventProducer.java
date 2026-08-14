package dev.jcasaslopez.classroom.producer;

import dev.jcasaslopez.classroom.shared.event.ClassroomEvent;

public interface ClassroomEventProducer {
	
	public void publishAllClassrooms();
	public void publishClassroom(ClassroomEvent classroom);
	public void sendTombstone(int idClassroom);
	
}
