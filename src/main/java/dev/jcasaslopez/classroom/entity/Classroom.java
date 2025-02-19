package dev.jcasaslopez.classroom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="classrooms")
public class Classroom {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idClassroom;
	@NotBlank
	@Column(unique = true)
	private String name;
	@Min(8)	
	private int seats;
	@NotNull
	private boolean projector;
	@NotNull
	private boolean speakers;
	
	public Classroom(int idClassroom, String name, int seats, boolean projector, boolean speakers) {
		this.idClassroom = idClassroom;
		this.name = name;
		this.seats = seats;
		this.projector = projector;
		this.speakers = speakers;
	}

	public Classroom() {
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
