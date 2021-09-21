package com.chalyk.classroom.service;

import com.chalyk.classroom.entity.Student;
import com.chalyk.classroom.repository.StudentRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Transactional
@Service
public class StudentService implements UserDetailsService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return studentRepository.findStudentByLogin(s).orElseThrow(() -> new UsernameNotFoundException("Not found User By Login"));
    }

    public Student findStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Not found User By Id"));
    }

    public Student findStudentByLogin(String login) {
        return studentRepository.findStudentByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Not found User By Login"));
    }

    public List<Student> findStudents() {
        return studentRepository.findAll();
    }

    public Student findAccountByPrincipal(Principal principal) {
        String login = principal.getName();
        return studentRepository.findStudentByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Not found User By Login"));
    }

    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
