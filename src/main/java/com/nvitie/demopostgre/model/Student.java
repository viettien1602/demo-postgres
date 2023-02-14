package com.nvitie.demopostgre.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nvitie.demopostgre.model.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Student implements Serializable {
    @Id
    @SequenceGenerator(
            name = "sequenceGenerator",
            initialValue = 1,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequenceGenerator"
    )
    private Long id;
    @Column(nullable = false)
    private String name;
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;
    private LocalDate birthdate;
    @Transient
    private Integer age;

    @Column(nullable = true, unique = true)
    private String phone;

    public Student(String name, String email, Gender gender, LocalDate birthdate, String phone) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.birthdate = birthdate;
        this.phone = phone;
    }

    public Integer getAge() {
        return Period.between(this.birthdate, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", birthdate=" + birthdate +
                ", phone='" + phone + '\'' +
                '}';
    }
}
