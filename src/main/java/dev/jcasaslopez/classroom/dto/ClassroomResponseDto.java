package dev.jcasaslopez.classroom.dto;

public record ClassroomResponseDto(int idClassroom, String name, int seats, boolean projector, boolean speakers) {}
