package com.chalyk.classroom.security;

public enum AuthConstants {

    SIGN_UP_URLS("/api/auth/**"),
    SECRET("SecretKeyGenJWT"),
    TOKEN_PREFIX("StudentPr "),
    HEADER_STRING("Authorization"),
    CONTENT_TYPE("application/json");

    private String title;

    AuthConstants(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
