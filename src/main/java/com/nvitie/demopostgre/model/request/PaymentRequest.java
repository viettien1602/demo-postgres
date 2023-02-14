package com.nvitie.demopostgre.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nvitie.demopostgre.model.Payment;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PaymentRequest {
    private final Payment payment;

    public PaymentRequest(@JsonProperty("payment") Payment payment) {
        this.payment = payment;
    }
}
