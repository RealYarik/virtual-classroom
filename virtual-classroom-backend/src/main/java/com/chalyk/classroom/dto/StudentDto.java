package com.chalyk.classroom.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class StudentDto {

    private Long id;

    @NotEmpty(message = "Login cannot be empty")
    @Size(min = 1, max = 50, message = "Login can be between 1 and 50 characters")
    private String login;

    private boolean isHandUp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isHandUp() {
        return isHandUp;
    }

    public void setHandUp(boolean handUp) {
        isHandUp = handUp;
    }
}
