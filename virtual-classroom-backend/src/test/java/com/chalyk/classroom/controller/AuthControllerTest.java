package com.chalyk.classroom.controller;

import com.chalyk.classroom.entity.Student;
import com.chalyk.classroom.facade.StudentFacade;
import com.chalyk.classroom.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    public static final String API_AUTH_LOGIN = "/api/auth/login";

    private final StudentRepository studentRepository;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final StudentFacade studentFacade;

    @Autowired
    public AuthControllerTest(
            StudentRepository studentRepository,
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            StudentFacade studentFacade) {
        this.studentRepository = studentRepository;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.studentFacade = studentFacade;
    }

    @AfterEach
    public void resetDb() {
        studentRepository.deleteAll();
    }

    @Test
    public void givenStudent_whenAuthenticateStudent_thenStatus200() throws Exception {
        Student student = createTestStudent("test");
        studentRepository.save(student);

        mockMvc.perform(
                post(API_AUTH_LOGIN)
                        .content(objectMapper.writeValueAsString(studentFacade.studentToStudentDto(student)))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());

    }

    @Test
    public void givenStudentWithLoginOver50Chars_whenAuthenticateStudent_thenError() throws Exception {
        Student student = createTestStudent("testtesttesttesttesttesttesttetestteststesttestttesttesttesttesttesttesttesttest");

        mockMvc.perform(
                post(API_AUTH_LOGIN)
                        .content(objectMapper.writeValueAsString(studentFacade.studentToStudentDto(student)))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(content().string("{\"Size\":\"Login can be between 1 and 50 characters\"}"));

    }

    @Test
    public void givenStudentWithEmptyLogin_whenAuthenticateStudent_thenError() throws Exception {
        Student student = createTestStudent("");

        mockMvc.perform(
                post(API_AUTH_LOGIN)
                        .content(objectMapper.writeValueAsString(studentFacade.studentToStudentDto(student)))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(content().string("{\"Size\":\"Login can be between 1 and 50 characters\",\"NotEmpty\":\"Login cannot be empty\"}"));

    }

    private Student createTestStudent(String login) {
        Student student = new Student();
        student.setLogin(login);
        student.setHandUp(false);

        return student;
    }
}
