package com.nvitie.demopostgre.service.stripe;

import com.nvitie.demopostgre.model.charger.CardPaymentCharge;
import com.nvitie.demopostgre.model.charger.CardPaymentCharger;
import com.nvitie.demopostgre.model.enums.Currency;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@ConditionalOnProperty(
        value = "stripe.enabled",
        havingValue = "false"
)
public class MockStripeService implements CardPaymentCharger {

    @Override
    public CardPaymentCharge chargeCard(String cardSource,
                                        BigDecimal amount,
                                        Currency currency,
                                        String description) {
        CardPaymentCharge cardPaymentCharge = new CardPaymentCharge(true);
        return cardPaymentCharge;
    }
}
