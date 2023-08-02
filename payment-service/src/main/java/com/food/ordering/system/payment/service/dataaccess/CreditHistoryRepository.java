package com.food.ordering.system.payment.service.dataaccess;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class CreditHistoryRepository extends RouteBuilder {

  // CreditHistory save(CreditHistory creditHistory)
  // Optional<List<CreditHistory>> findByCustomerId(CustomerId id);
  @Override
  public void configure() throws Exception {

  }
}
