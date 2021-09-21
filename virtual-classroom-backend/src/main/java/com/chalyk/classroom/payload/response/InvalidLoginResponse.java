package com.chalyk.classroom.payload.response;

public class InvalidLoginResponse {

    private String message;

    public InvalidLoginResponse() {
        this.message = "Invalid login";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
