package com.slupicki.lideo.dao;

import com.slupicki.lideo.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
