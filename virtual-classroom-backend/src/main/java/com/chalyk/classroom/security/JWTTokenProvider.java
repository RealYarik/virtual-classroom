package com.chalyk.classroom.security;

import com.chalyk.classroom.entity.Student;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTokenProvider {

    public String generateToken(Authentication authentication) {
        Student student = (Student) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());

        String studentId = Long.toString(student.getId());

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", studentId);
        claimsMap.put("login", student.getLogin());

        return Jwts.builder()
                .setSubject(studentId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS512, AuthConstants.SECRET.getTitle())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(AuthConstants.SECRET.getTitle())
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException |
                MalformedJwtException |
                UnsupportedJwtException |
                IllegalArgumentException exception
        ) {
            return false;
        }

    }

    public Long getStudentIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(AuthConstants.SECRET.getTitle())
                .parseClaimsJws(token)
                .getBody();
        String id = (String) claims.get("id");

        return Long.parseLong(id);
    }

}
