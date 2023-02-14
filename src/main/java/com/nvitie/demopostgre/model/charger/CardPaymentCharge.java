package com.nvitie.demopostgre.model.charger;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CardPaymentCharge {
    private final boolean isCardDebited;

    public CardPaymentCharge(boolean isCardDebited) {
        this.isCardDebited = isCardDebited;
    }
}
