package dev.jcasaslopez.classroom.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Se utilizan tipos envoltorios (Integer, Boolean) en lugar de primitivos, ya que los tipos 
// primitivos no pueden ser null y Jackson asignaría automáticamente valores por defecto 
// (por ejemplo, false para booleanos) al deserializar JSON. Esto impediría detectar campos 
// ausentes y realizar validaciones como @NotNull correctamente.

// Wrapper types (Integer, Boolean) are used instead of primitive types because primitives 
// cannot be null, and Jackson would automatically assign default values (e.g., false for 
// booleans) when deserializing JSON. This would prevent detecting missing fields and 
// correctly performing validations such as @NotNull.

public class ClassroomDto {
	
	private int idClassroom;
	@NotBlank
	private String name;
	@Min(8)	
	private Integer seats;
	@NotNull
	private Boolean projector;
	@NotNull
	private Boolean speakers;
	
	public ClassroomDto(int idClassroom, @NotBlank String name, @Min(8) Integer seats, @NotNull Boolean projector,
			@NotNull Boolean speakers) {
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

	public Integer getSeats() {
		return seats;
	}

	public void setSeats(Integer seats) {
		this.seats = seats;
	}

	public Boolean getProjector() {
		return projector;
	}

	public void setProjector(Boolean projector) {
		this.projector = projector;
	}

	public Boolean getSpeakers() {
		return speakers;
	}

	public void setSpeakers(Boolean speakers) {
		this.speakers = speakers;
	}
	
}
