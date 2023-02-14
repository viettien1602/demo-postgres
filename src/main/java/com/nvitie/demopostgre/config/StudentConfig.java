package com.nvitie.demopostgre.config;

import com.nvitie.demopostgre.model.Payment;
import com.nvitie.demopostgre.model.Student;
import com.nvitie.demopostgre.model.enums.Currency;
import com.nvitie.demopostgre.model.enums.Gender;
import com.nvitie.demopostgre.repository.PaymentRepository;
import com.nvitie.demopostgre.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
@Slf4j
public class StudentConfig {
//    Logger logger = LoggerFactory.getLogger(StudentConfig.class);
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository, PaymentRepository paymentRepository) {
        return args -> {
            //logging test
            log.trace("A trace message");
            log.debug("A debug message");
            log.info("An info message");
            log.warn("A warn message");
            log.error("An error message");

            //init to database
            Student student1 = new Student(
                    "tien1",
                    "tien1@gmail.com",
                    Gender.MALE,
                    LocalDate.of(2002, Month.MARCH,16),
                    "0903678282"
            );
            Student student2 = new Student(
                    "tien2",
                    "tien2@gmail.com",
                    Gender.MALE,
                    LocalDate.of(2002, 2,16),
                    "0903678281"
            );
            studentRepository.saveAll(List.of(student1, student2));
            Payment payment1 = new Payment(
                    student1,
                    new BigDecimal(10000),
                    Currency.USD,
                    "Own source",
                    "No description"
            );
            paymentRepository.save(payment1);
        };
    }
}
