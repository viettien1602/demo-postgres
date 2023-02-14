package com.nvitie.demopostgre.controller;

import com.nvitie.demopostgre.model.Student;
import com.nvitie.demopostgre.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
}

    @GetMapping()
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping()
    public void addStudent(@Valid @RequestBody Student student) {
        studentService.addStudent(student);
    }

    @DeleteMapping("/{studentId}")

    public void deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
    }

}
