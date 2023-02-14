package com.nvitie.demopostgre.service;

import com.nvitie.demopostgre.exception.BadRequestException;
import com.nvitie.demopostgre.exception.StudentNotFoundException;
import com.nvitie.demopostgre.model.Payment;
import com.nvitie.demopostgre.model.Student;
import com.nvitie.demopostgre.model.charger.CardPaymentCharge;
import com.nvitie.demopostgre.model.charger.CardPaymentCharger;
import com.nvitie.demopostgre.model.enums.Currency;
import com.nvitie.demopostgre.model.enums.Gender;
import com.nvitie.demopostgre.model.request.PaymentRequest;
import com.nvitie.demopostgre.repository.PaymentRepository;
import com.nvitie.demopostgre.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CardPaymentCharger cardPaymentCharger;
    private PaymentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new PaymentService(
                paymentRepository, 
                studentRepository,
                cardPaymentCharger
        );
    }

    @Test
    void canChargeCard() {
        //given
        Long studentId = 1L;
        Student student = new Student(
                "tien",
                "viettien@gmail.com",
                Gender.MALE,
                LocalDate.of(2002, Month.FEBRUARY, 16),
                "0903678282"
        );
        student.setId(studentId);
            //customer exist
        Mockito.when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(student));
            //payment request
        PaymentRequest paymentRequest = new PaymentRequest(
                new Payment(
                    student,
                    new BigDecimal("100.00"),
                    Currency.USD,
                    "card123xxx",
                    "Donation"
                )
        );
        Mockito.when(cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription()
        )).thenReturn(new CardPaymentCharge(true));
        //when
        underTest.chargeCard(studentId, paymentRequest);
        //then
        ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);
        Mockito.verify(paymentRepository).save(paymentArgumentCaptor.capture());
        Payment paymentCaptor = paymentArgumentCaptor.getValue();
        Assertions.assertThat(paymentCaptor).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(paymentRequest.getPayment());
    }

    @Test
    void cannotChargeCardBecauseStudentIdNotExist() {
        //given
        Long studentId = 1L;
        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.chargeCard(studentId, new PaymentRequest(new Payment())))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + studentId + " does not exist");
        Mockito.verifyNoInteractions(cardPaymentCharger);
        Mockito.verify(paymentRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void cannotChargeCardBecauseCurrencyIsNotSupported() {
        //given
        Long studentId = 1L;
        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(Mockito.mock(Student.class)));
        PaymentRequest paymentRequest = new PaymentRequest(
                new Payment()
        );
        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.chargeCard(studentId, paymentRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Currency " + paymentRequest.getPayment().getCurrency() + "do not support");
        Mockito.verifyNoInteractions(cardPaymentCharger);
        Mockito.verify(paymentRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void cannotChargeCardBecauseCardIsNotDebited() {
        //given
        Long studentId = 1L;
        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(Mockito.mock(Student.class)));
        PaymentRequest paymentRequest = new PaymentRequest(new Payment(
                null,
                null,
                Currency.USD,
                null,
                null
        ));
        Mockito.when(cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription()
        )).thenReturn(new CardPaymentCharge(false));
        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.chargeCard(studentId, paymentRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Card not debited for student " + studentId);
        Mockito.verify(paymentRepository, Mockito.never()).save(Mockito.any());
    }
}