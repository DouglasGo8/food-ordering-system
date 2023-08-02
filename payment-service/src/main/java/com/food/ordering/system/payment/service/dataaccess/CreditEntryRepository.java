package com.food.ordering.system.payment.service.dataaccess;

import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;


@AllArgsConstructor
@ApplicationScoped
public class CreditEntryRepository extends RouteBuilder {

  // CreditEntry save(CreditEntry creditEntry)
  // Optional<CreditEntry> findByCustomerId(CustomerId customerId)
  @Override
  public void configure() {

  }
}
