package com.nvitie.demopostgre.service;

import com.nvitie.demopostgre.exception.BadRequestException;
import com.nvitie.demopostgre.exception.StudentNotFoundException;
import com.nvitie.demopostgre.model.Student;
import com.nvitie.demopostgre.model.charger.CardPaymentCharge;
import com.nvitie.demopostgre.model.charger.CardPaymentCharger;
import com.nvitie.demopostgre.model.enums.Currency;
import com.nvitie.demopostgre.model.request.PaymentRequest;
import com.nvitie.demopostgre.repository.PaymentRepository;
import com.nvitie.demopostgre.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final StudentRepository studentRepository;
    private final PaymentRepository paymentRepository;
    private final CardPaymentCharger cardPaymentCharger;


    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          StudentRepository studentRepository,
                          CardPaymentCharger cardPaymentCharger) {
        this.paymentRepository = paymentRepository;
        this.studentRepository = studentRepository;
        this.cardPaymentCharger = cardPaymentCharger;
    }
    public void chargeCard(Long studentId, PaymentRequest paymentRequest) {
        Optional<Student> isExistStudent = studentRepository.findById(studentId);
        if (isExistStudent.isEmpty())
            throw new StudentNotFoundException("Student with id " + studentId + " does not exist");
        List<Currency> listCurrency = Arrays.asList(Currency.values());
        if (!listCurrency.contains(paymentRequest.getPayment().getCurrency()))
            throw new BadRequestException("Currency " + paymentRequest.getPayment().getCurrency() + "do not support");

        CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription()
        );

        if (!cardPaymentCharge.isCardDebited()) {
            throw new IllegalStateException("Card not debited for student " + studentId);
        }

        paymentRequest.getPayment().setStudent(isExistStudent.get());
        paymentRepository.save(paymentRequest.getPayment());
    }
}
