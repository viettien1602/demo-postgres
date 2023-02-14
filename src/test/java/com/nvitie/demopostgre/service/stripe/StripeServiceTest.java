package com.nvitie.demopostgre.service.stripe;

import com.nvitie.demopostgre.model.charger.CardPaymentCharge;
import com.nvitie.demopostgre.model.enums.Currency;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StripeServiceTest {
    private StripeService underTest;
    @Mock
    private StripeApi stripeApi;
    @BeforeEach
    void setUp() {
        underTest = new StripeService(stripeApi);
    }

    @Test
    void canChargeCard() throws StripeException {
        //given
        String cardSource = "0x0x0x";
        BigDecimal amount = new BigDecimal("10.00");
        Currency currency = Currency.USD;
        String description = "Charge card in stripe service test";
        Charge charge = new Charge();
        charge.setPaid(true);
        Mockito.when(stripeApi.create(Mockito.anyMap(), Mockito.any(RequestOptions.class)))
                .thenReturn(charge);
        //when
        CardPaymentCharge cardPaymentCharge = underTest.chargeCard(cardSource, amount, currency, description);
        //then
        ArgumentCaptor<Map<String, Object>> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<RequestOptions> optionsArgumentCaptor = ArgumentCaptor.forClass(RequestOptions.class);
        Mockito.verify(stripeApi).create(mapArgumentCaptor.capture(), optionsArgumentCaptor.capture());
        Map<String, Object> params = mapArgumentCaptor.getValue();
        RequestOptions requestOptions = optionsArgumentCaptor.getValue();

        Assertions.assertThat(params.keySet()).hasSize(4);
        Assertions.assertThat(params.get("amount")).isEqualTo(amount);
        Assertions.assertThat(params.get("currency")).isEqualTo(currency);
        Assertions.assertThat(params.get("source")).isEqualTo(cardSource);
        Assertions.assertThat(params.get("description")).isEqualTo(description);

        Assertions.assertThat(requestOptions).isNotNull();

        Assertions.assertThat(cardPaymentCharge.isCardDebited()).isTrue();
    }

    @Test
    void cannotChargeCardAndThrowException() throws StripeException {
        //given
        String cardSource = "0x0x0x";
        BigDecimal amount = new BigDecimal("10.00");
        Currency currency = Currency.USD;
        String description = "Charge card in stripe service test";
        Mockito.when(stripeApi.create(Mockito.anyMap(), Mockito.any(RequestOptions.class)))
                        .thenThrow(new StripeException("", "", "", 0) {});
        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.chargeCard(
                cardSource,
                amount,
                currency,
                description
        ))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot make stripe charge");
    }
}