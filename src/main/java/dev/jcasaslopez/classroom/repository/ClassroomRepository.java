package dev.jcasaslopez.classroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.jcasaslopez.classroom.entity.Classroom;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {

}
