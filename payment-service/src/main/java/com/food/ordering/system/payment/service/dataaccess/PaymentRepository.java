package com.food.ordering.system.payment.service.dataaccess;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;


@NoArgsConstructor
@ApplicationScoped
public class PaymentRepository extends RouteBuilder {


  // Payment save(Payment payment)
  @Override
  public void configure() {

    from("direct:savePayment").routeId("SavePaymentRouteId")
            .transform(exchangeProperty("payment"))
            .to("sql-stored:classpath:templates/insertPayment.sql")
            //.log("${body.price}")
            //.transform(constant("Save the Payment"))
            .end();


  }
}
