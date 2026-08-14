package dev.jcasaslopez.classroom.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Wrapper types (Integer, Boolean) are used instead of primitive types because primitives cannot be null, 
// and Jackson would automatically assign default values (e.g., false for booleans) when deserializing JSON. 
// This would prevent detecting missing fields and correctly performing validations such as @NotNull.
public record ClassroomRequestDto(
		
		@NotBlank(message = "Classroom name is required")
        @Size(max = 15, message = "Classroom name must be at most 15 characters")
        String name,

        @NotNull(message = "Seats field is required")
        @Max(value = 300, message = "Seats must be at most 300")
        Integer seats,

        @NotNull(message = "Projector field is required")
        Boolean projector,

        @NotNull(message = "Speakers field is required")
        Boolean speakers
		) {

}
