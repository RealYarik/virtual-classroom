package com.chalyk.classroom.controller;

import com.chalyk.classroom.entity.Student;
import com.chalyk.classroom.facade.StudentFacade;
import com.chalyk.classroom.repository.StudentRepository;
import com.chalyk.classroom.security.AuthConstants;
import com.chalyk.classroom.security.AuthenticationToken;
import com.chalyk.classroom.security.JWTTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerTest {

    public static final String API_STUDENTS = "/api/students/";

    private final StudentRepository studentRepository;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final StudentFacade studentFacade;
    private final JWTTokenProvider jwtTokenProvider;

    @Autowired
    public StudentControllerTest(
            StudentRepository studentRepository,
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            StudentFacade studentFacade,
            JWTTokenProvider jwtTokenProvider) {
        this.studentRepository = studentRepository;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.studentFacade = studentFacade;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @AfterEach
    public void resetDb() {
        studentRepository.deleteAll();
    }

    @Test
    public void whenGetStudents_thenStatus200AndListOfStudents() throws Exception {
        Student student = createTestStudent("test");
        studentRepository.save(student);

        AuthenticationToken authenticationToken = new AuthenticationToken(student);

        mockMvc.perform(
                get(API_STUDENTS)
                        .header(AuthConstants.HEADER_STRING.getTitle(), AuthConstants.TOKEN_PREFIX.getTitle() + jwtTokenProvider.generateToken(authenticationToken))
        )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(studentFacade.studentToStudentDto(student)))));
    }

    @Test
    public void whenGetCurrentStudent_thenStatus200AndCurrentStudent() throws Exception {
        Student student = createTestStudent("test");
        studentRepository.save(student);

        AuthenticationToken authenticationToken = new AuthenticationToken(student);

        mockMvc.perform(
                get(API_STUDENTS + "current")
                        .header(AuthConstants.TOKEN_PREFIX.getTitle(), AuthConstants.TOKEN_PREFIX.getTitle() + jwtTokenProvider.generateToken(authenticationToken))
        )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(studentFacade.studentToStudentDto(student))));
    }

    private Student createTestStudent(String login) {
        Student student = new Student();
        student.setLogin(login);
        student.setHandUp(false);

        return student;
    }
}
