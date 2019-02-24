package com.slupicki.lideo.rest;

import com.slupicki.lideo.dao.PaymentRepository;
import com.slupicki.lideo.exceptions.AlreadyExistException;
import com.slupicki.lideo.exceptions.NotFoundException;
import com.slupicki.lideo.model.Payment;
import com.slupicki.lideo.model.PaymentDTO;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/payment")
public class PaymentController {

  private final PaymentRepository paymentRepository;

  public PaymentController(PaymentRepository paymentRepository) {
    this.paymentRepository = paymentRepository;
  }

  @GetMapping("")
  public List<Payment> getAllPayments() {
    return paymentRepository.findAll();
  }

  @PutMapping("")
  @Transactional
  public void paid(@RequestBody PaymentDTO paymentDTO) throws NotFoundException, AlreadyExistException {
    Assert.notNull(paymentDTO.getPaymentId(), "Payment ID have to be set");
    Payment payment = paymentRepository.findById(paymentDTO.getPaymentId())
        .orElseThrow(() -> new NotFoundException("Can't find payment with ID = " + paymentDTO.getPaymentId()));
    if (payment.getPaid()) {
      throw new AlreadyExistException("Payment with ID = " + paymentDTO.getPaymentId() + " already paid");
    }
    payment.setPaid(true);
    paymentRepository.save(payment);
  }

  @GetMapping("/template")
  public PaymentDTO getTemplate() {
    return PaymentDTO.builder()
        .paymentId(1L)
        .externalId("Some external ID")
        .build();
  }
}
