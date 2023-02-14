package com.nvitie.demopostgre.repository;

import com.nvitie.demopostgre.model.Student;
import com.nvitie.demopostgre.model.enums.Gender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

//import static org.junit.jupiter.api.Assertions.*;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.*;
@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @BeforeEach
    void setUp() {
        Student student = new Student(
                "viettien",
                "viettien@gmail.com",
                Gender.MALE,
                LocalDate.of(2002, Month.FEBRUARY, 16),
                "0903678282"
        );
        underTest.save(student);
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }



    @Test
    void itShouldReturnStudentWithExistPhone() {
        //given
        String testPhone = "0903678282";
        Student testStudent = new Student(
                "viettien",
                "viettien@gmail.com",
                Gender.MALE,
                LocalDate.of(2002, Month.FEBRUARY, 16),
                "0903678282"
        );
        //when
        Optional<Student> student = underTest.findByPhone(testPhone);
        //then
        Assertions.assertThat(student)
                .isPresent()
                .hasValueSatisfying(s -> {
                   Assertions.assertThat(s).usingRecursiveComparison()
                           .ignoringFields("id")
                           .isEqualTo(testStudent);
                });
    }

    @Test
    void itShouldNotReturnStudentWithNotExistPhone() {
        //given
        String testPhone = "0903678281";
        //when
        Optional<Student> student = underTest.findByPhone(testPhone);
        //then
        Assertions.assertThat(student).isEmpty();

    }

    @Test
    void isShouldReturnStudentWithExistedEmail() {
        //when
        String testName = "viettien";
        String testEmail = "viettien@gmail.com";
        Gender testGender = Gender.MALE;
        LocalDate testBirthdate = LocalDate.of(2002, Month.FEBRUARY, 16);
        String testPhone = "090367282";
        Optional<Student> checkExistStudent = underTest.findByEmail(testEmail);
        //then
        assertThat(checkExistStudent)
                .isPresent()
                .hasValueSatisfying(s -> {
                    assertThat(s.getName()).isEqualTo(testName);
                    assertThat(s.getEmail()).isEqualTo(testEmail);
                    assertThat(s.getGender()).isEqualTo(testGender);
                    assertThat(s.getBirthdate()).isEqualTo(testBirthdate);
                });
    }

    @Test
    void isShouldNotReturnStudentWithNotExistedEmail() {
        //when
        String testEmail = "vietien@gmail.com";
        Optional<Student> checkExistStudent = underTest.findByEmail(testEmail);
        //then
        assertThat(checkExistStudent).isEmpty();
    }



    @Test
    void itShouldCheckIfEmailExists() {
        //when
        String testEmail = "viettien@gmail.com";
        Boolean actualExist = underTest.checkExistsByEmail(testEmail);
        //then
        assertThat(actualExist).isTrue();
    }

    @Test
    void itShouldCheckIfEmailNotExist() {
        //when
        String testEmail = "vietien@gmail.com";
        Boolean actualExist = underTest.checkExistsByEmail(testEmail);
        //then
        assertThat(actualExist).isFalse();
    }
}