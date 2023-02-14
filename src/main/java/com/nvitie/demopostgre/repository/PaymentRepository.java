package com.nvitie.demopostgre.repository;

import com.nvitie.demopostgre.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
