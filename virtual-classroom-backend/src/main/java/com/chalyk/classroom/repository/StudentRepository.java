package com.chalyk.classroom.repository;

import com.chalyk.classroom.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findStudentByLogin(String login);

}
