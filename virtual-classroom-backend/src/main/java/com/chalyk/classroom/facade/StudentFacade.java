package com.chalyk.classroom.facade;

import com.chalyk.classroom.dto.StudentDto;
import com.chalyk.classroom.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentFacade {

    public StudentDto studentToStudentDto(Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(student.getId());
        studentDto.setLogin(student.getLogin());
        studentDto.setHandUp(student.isHandUp());

        return studentDto;
    }
}
