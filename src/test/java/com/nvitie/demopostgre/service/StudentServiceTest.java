package com.nvitie.demopostgre.service;

import com.nvitie.demopostgre.exception.BadRequestException;
import com.nvitie.demopostgre.exception.StudentNotFoundException;
import com.nvitie.demopostgre.model.Student;
import com.nvitie.demopostgre.model.enums.Gender;
import com.nvitie.demopostgre.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository);
    }

    @Test
    void canGetAllStudents() {
        //when
        underTest.getAllStudents();
        //then
        Mockito.verify(studentRepository).findAll();
    }

    @Test
    void canAddStudent() {
        //given
        Student student = new Student(
                "viettien",
                "viettien1602@gmail.com",
                Gender.MALE,
                LocalDate.of(2002, Month.FEBRUARY, 16),
                "0903678282"
        );
        //when
        underTest.addStudent(student);
        //then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        Mockito.verify(studentRepository).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        Assertions.assertThat(capturedStudent).isEqualTo(student);

    }

    @Test
    void cannotAddStudentSinceEmailIsTaken() {
        //given
        Student student = new Student(
                "viettien",
                "viettien@gmail.com",
                Gender.MALE,
                LocalDate.of(2002, Month.FEBRUARY, 16),
                "0903678282"
        );
        Mockito.when(studentRepository.checkExistsByEmail(ArgumentMatchers.anyString()))
                        .thenReturn(true);
        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");
        Mockito.verify(studentRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test
    void cannotAddStudentSincePhoneIsTaken() {
        //given
        Student student = new Student(
                "viettien",
                "viettien@gmail.com",
                Gender.MALE,
                LocalDate.of(2002, Month.FEBRUARY, 16),
                "0903678282"
        );
        Mockito.when(studentRepository.findByPhone(student.getPhone()))
                .thenReturn(Optional.of(new Student()));
        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Phone " + student.getPhone() + "taken");
        Mockito.verify(studentRepository, Mockito.never()).save(ArgumentMatchers.any());

    }

    @Test
    void canDeleteStudentByExistId() {
        //given
        Long studentId = 1L;
        BDDMockito.given(studentRepository.existsById(studentId))
                .willReturn(true);
        //when
        underTest.deleteStudent(studentId);
        //then
        ArgumentCaptor<Long> studentIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(studentRepository).deleteById(studentIdArgumentCaptor.capture());
        Long capturedStudentId = studentIdArgumentCaptor.getValue();
        Assertions.assertThat(capturedStudentId).isEqualTo(studentId);
    }

    @Test
    void cannotDeleteStudentByNotExistId() {
        //given
        Long studentId = 1L;
        Mockito.when(studentRepository.existsById(studentId))
                .thenReturn(false);
        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.deleteStudent(studentId))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + studentId + " does not exist");
        Mockito.verify(studentRepository, Mockito.never()).deleteById(ArgumentMatchers.anyLong());
    }
}