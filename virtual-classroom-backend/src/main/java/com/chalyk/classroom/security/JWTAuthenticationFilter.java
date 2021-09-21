package com.chalyk.classroom.security;

import com.chalyk.classroom.entity.Student;
import com.chalyk.classroom.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    public static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private final JWTTokenProvider jwtTokenProvider;
    private final StudentService studentService;

    @Autowired
    public JWTAuthenticationFilter(JWTTokenProvider jwtTokenProvider, StudentService studentService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.studentService = studentService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJWTFromRequest(httpServletRequest);

            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                Long studentId = jwtTokenProvider.getStudentIdFromToken(jwt);
                Student student = studentService.findStudentById(studentId);

                AuthenticationToken authenticationToken = new AuthenticationToken(student);


                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            LOGGER.error("Could not set student auth");
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String taskToken = request.getHeader(AuthConstants.HEADER_STRING.getTitle());

        if (StringUtils.hasText(taskToken) && taskToken.startsWith(AuthConstants.TOKEN_PREFIX.getTitle())) {
            return taskToken.split(" ")[1];
        }
        return null;
    }
}
