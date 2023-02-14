package com.nvitie.demopostgre.controller;

import com.nvitie.demopostgre.model.request.PaymentRequest;
import com.nvitie.demopostgre.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{studentId}")
    public void makePayment(@PathVariable("studentId") Long studentId,
                            @Valid @RequestBody PaymentRequest paymentRequest) {
        paymentService.chargeCard(studentId, paymentRequest);
    }
}
