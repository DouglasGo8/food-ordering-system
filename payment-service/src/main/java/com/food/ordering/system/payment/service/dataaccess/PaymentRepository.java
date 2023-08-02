package com.food.ordering.system.payment.service.dataaccess;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;


@NoArgsConstructor
@AllArgsConstructor
public class PaymentRepository extends RouteBuilder {


  // Payment save(Payment payment)
  // Optional<Payment> findByOrderId(UUID orderId)
  @Override
  public void configure() {

  }
}
