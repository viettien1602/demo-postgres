package com.nvitie.demopostgre.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nvitie.demopostgre.model.Payment;
import com.nvitie.demopostgre.model.Student;
import com.nvitie.demopostgre.model.enums.Currency;
import com.nvitie.demopostgre.model.enums.Gender;
import com.nvitie.demopostgre.model.request.PaymentRequest;
import com.nvitie.demopostgre.repository.PaymentRepository;
import com.nvitie.demopostgre.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentIntegrationTest {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private MockMvc mockMvc;
    @Test
    void canMakePayment() throws Exception {
        //given
        Student testStudent = new Student(
                "viettien",
                "viettien@gmail.com",
                Gender.MALE,
                LocalDate.of(2002, Month.MARCH,16),
                "0123456789"
        );
        testStudent.setId(1L);
        String jsonTestStudent = null;
        try {
            jsonTestStudent = objectToJson(testStudent);
        }
        catch (JsonProcessingException e) {
            Assertions.fail("Failed to convert object to json \n" + e.getMessage());
        }

        Payment testPayment = new Payment(
                testStudent,
                new BigDecimal("100.00"),
                Currency.USD,
                "card123xxx",
                "donation"
        );
        testPayment.setId(3L);
        String jsonPaymentRequest = null;
        try {
            jsonPaymentRequest = objectToJson(new PaymentRequest(testPayment));
        }
        catch (JsonProcessingException e) {
            Assertions.fail("Failed to convert object to json \n" + e.getMessage());
        }

        //when
        String addStudentUrl = "/api/v1/student";
        ResultActions addStudentResultActions = mockMvc.perform(post(addStudentUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(jsonTestStudent))
        );

        String makePaymentUrl = "/api/v1/payment/{studentId}";
        ResultActions makePaymentResultActions = mockMvc.perform(post(makePaymentUrl, testStudent.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(jsonPaymentRequest))
        );

        //then
        addStudentResultActions.andExpect(status().isOk());
        Assertions.assertThat(studentRepository.findById(testStudent.getId()))
                        .isPresent()
                        .hasValueSatisfying(s -> {
                            Assertions.assertThat(s).usingRecursiveComparison()
                                    .isEqualTo(testStudent);
                        });

        makePaymentResultActions.andExpect(status().isOk());
        Assertions.assertThat(paymentRepository.findById(testPayment.getId()))
                .isPresent()
                .hasValueSatisfying(p -> {
                    Assertions.assertThat(p).usingRecursiveComparison()
                            .isEqualTo(testPayment);
                });
    }

    //generic method to convert object to json
    private <T> String objectToJson(T object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.writeValueAsString(object);
    }
}
