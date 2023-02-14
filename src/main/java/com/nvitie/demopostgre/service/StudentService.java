package com.nvitie.demopostgre.service;

import com.nvitie.demopostgre.exception.BadRequestException;
import com.nvitie.demopostgre.exception.StudentNotFoundException;
import com.nvitie.demopostgre.model.Student;
import com.nvitie.demopostgre.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void addStudent(Student student) {
        Boolean existsEmail = studentRepository
                .checkExistsByEmail(student.getEmail());
        if (existsEmail)
            throw new BadRequestException("Email " + student.getEmail() + " taken");
        Optional<Student> existStudentWithPhone = studentRepository
                .findByPhone(student.getPhone());
        if (existStudentWithPhone.isPresent())
            throw new BadRequestException("Phone " + student.getPhone() + "taken");

        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        if(!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(
                    "Student with id " + studentId + " does not exist");
        }
        studentRepository.deleteById(studentId);
    }


}
