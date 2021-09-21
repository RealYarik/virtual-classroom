package com.chalyk.classroom.controller;

import com.chalyk.classroom.dto.StudentDto;
import com.chalyk.classroom.entity.Student;
import com.chalyk.classroom.facade.StudentFacade;
import com.chalyk.classroom.payload.response.JWTTokenSuccessResponse;
import com.chalyk.classroom.security.AuthConstants;
import com.chalyk.classroom.security.JWTTokenProvider;
import com.chalyk.classroom.security.AuthenticationToken;
import com.chalyk.classroom.service.ResponseErrorValidation;
import com.chalyk.classroom.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final ResponseErrorValidation responseErrorValidation;
    private final StudentService studentService;
    private final JWTTokenProvider jwtTokenProvider;
    private final SimpMessagingTemplate messagingTemplate;
    private final StudentFacade studentFacade;

    public AuthController(
            ResponseErrorValidation responseErrorValidation,
            StudentService studentService,
            JWTTokenProvider jwtTokenProvider,
            SimpMessagingTemplate messagingTemplate,
            StudentFacade studentFacade
    ) {
        this.responseErrorValidation = responseErrorValidation;
        this.studentService = studentService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.messagingTemplate = messagingTemplate;
        this.studentFacade = studentFacade;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticateStudent(@Valid @RequestBody StudentDto studentDto, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);

        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        Student student;

        try {
            student = studentService.findStudentByLogin(studentDto.getLogin());
        } catch (UsernameNotFoundException exception) {
            student = new Student();
            student.setLogin(studentDto.getLogin());
            student.setHandUp(false);

            studentService.saveStudent(student);
            student = studentService.findStudentByLogin(studentDto.getLogin());

            messagingTemplate.convertAndSend("/queue/newStudent/", studentFacade.studentToStudentDto(student));
        }

        AuthenticationToken authenticationToken = new AuthenticationToken(student);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String jwt = AuthConstants.TOKEN_PREFIX.getTitle() + jwtTokenProvider.generateToken(authenticationToken);

        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));
    }
}
