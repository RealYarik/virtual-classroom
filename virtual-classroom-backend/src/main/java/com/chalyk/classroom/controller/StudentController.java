package com.chalyk.classroom.controller;

import com.chalyk.classroom.dto.StudentDto;
import com.chalyk.classroom.entity.Student;
import com.chalyk.classroom.facade.StudentFacade;
import com.chalyk.classroom.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@CrossOrigin
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final StudentFacade studentFacade;
    private final SimpMessagingTemplate messagingTemplate;

    public StudentController(
            StudentService studentService,
            StudentFacade studentFacade,
            SimpMessagingTemplate messagingTemplate) {
        this.studentService = studentService;
        this.studentFacade = studentFacade;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getStudents() {
        List<StudentDto> studentsDto = studentService.findStudents().stream()
                .map(studentFacade::studentToStudentDto)
                .collect(toList());

        return new ResponseEntity<>(studentsDto, HttpStatus.OK);
    }

    @GetMapping("/current")
    public ResponseEntity<StudentDto> getCurrentStudent(Principal principal) {
        Student student = studentService.findAccountByPrincipal(principal);
        StudentDto studentDto = studentFacade.studentToStudentDto(student);

        return new ResponseEntity<>(studentDto, HttpStatus.OK);
    }

    @MessageMapping("/send/hand/")
    public void sendHand(StudentDto studentDto) {
        Student student = studentService.findStudentByLogin(studentDto.getLogin());
        student.setHandUp(!student.isHandUp());

        studentService.saveStudent(student);
        messagingTemplate.convertAndSend("/queue/reply/", studentFacade.studentToStudentDto(student));
    }
}
