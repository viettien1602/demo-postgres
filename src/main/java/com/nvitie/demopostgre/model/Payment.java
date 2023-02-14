package com.nvitie.demopostgre.model;

import com.nvitie.demopostgre.model.enums.Currency;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table
public class Payment {
    @Id
    @SequenceGenerator(
            name = "sequenceGenerator",
            initialValue = 1,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequenceGenerator"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;
    private String source;
    private String description;

    public Payment(Student student, BigDecimal amount, Currency currency, String source, String description) {
        this.student = student;
        this.amount = amount;
        this.currency = currency;
        this.source = source;
        this.description = description;
    }
}
