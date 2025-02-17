package dev.jcasaslopez.classroom.dto;

public class ClassroomDto {
	private int idClassroom;
	private String name;
	private int seats;
	private boolean projector;
	private boolean speakers;
	
	public ClassroomDto(int idClassroom, String name, int seats, boolean projector, boolean speakers) {
		this.idClassroom = idClassroom;
		this.name = name;
		this.seats = seats;
		this.projector = projector;
		this.speakers = speakers;
	}

	public ClassroomDto() {
		super();
	}

	public int getIdClassroom() {
		return idClassroom;
	}

	public void setIdClassroom(int idClassroom) {
		this.idClassroom = idClassroom;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public boolean isProjector() {
		return projector;
	}

	public void setProjector(boolean projector) {
		this.projector = projector;
	}

	public boolean isSpeakers() {
		return speakers;
	}

	public void setSpeakers(boolean speakers) {
		this.speakers = speakers;
	}
	
}
