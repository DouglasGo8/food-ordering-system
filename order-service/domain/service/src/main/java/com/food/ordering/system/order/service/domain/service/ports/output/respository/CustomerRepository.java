package com.food.ordering.system.order.service.domain.service.ports.output.respository;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class CustomerRepository extends RouteBuilder {
  @Override
  public void configure() throws Exception {
    // direct:findCustomer(UUID customerId)/Jdbc Component in Postgres Function returning Optional<Customer>
  }
}
