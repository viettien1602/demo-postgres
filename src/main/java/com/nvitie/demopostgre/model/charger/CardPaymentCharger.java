package com.nvitie.demopostgre.model.charger;

import com.nvitie.demopostgre.model.enums.Currency;

import java.math.BigDecimal;

public interface CardPaymentCharger {
    CardPaymentCharge chargeCard(String cardSource,
                                 BigDecimal amount,
                                 Currency currency,
                                 String description);
}
