package com.food.ordering.system.payment.service;

import io.quarkus.test.junit.QuarkusTest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@Slf4j
@QuarkusTest

public class CamelAppIT extends CamelQuarkusTestSupport implements BaseTest {

  @Inject
  ProducerTemplate producerTemplate;


  @Test
  @SneakyThrows
  public void persistPaymentRouteRepresentation() {

    AdviceWith.adviceWith(super.context, "PersistPaymentRouter", r -> r.weaveAddLast().to("mock:result"));
    //
    var body = this.createPaymentRequest();
    var mock = super.getMockEndpoint("mock:result");
    this.producerTemplate.sendBody("direct:persistPayment", body);
    //
    mock.setExpectedMessageCount(1);
    //
    mock.assertIsSatisfied();
  }

  @Test
  @Disabled
  @SneakyThrows
  public void persistCancelPaymentRouteRepresentation() {
    var body = this.createPaymentRequest();
    this.producerTemplate.sendBody("direct:persistCancelPayment", body);
    //
  }
}
