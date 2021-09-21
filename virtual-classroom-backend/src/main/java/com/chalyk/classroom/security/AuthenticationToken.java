package com.chalyk.classroom.security;

import com.chalyk.classroom.entity.Student;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class AuthenticationToken extends AbstractAuthenticationToken {

    private final Student student;

    public AuthenticationToken(Student student) {
        super(student.getAuthorities());
        this.student = student;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return "N/A";
    }

    @Override
    public Object getPrincipal() {
        return this.student;
    }
}
