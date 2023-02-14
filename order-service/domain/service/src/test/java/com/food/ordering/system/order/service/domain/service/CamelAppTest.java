package com.food.ordering.system.order.service.domain.service;


import io.quarkus.test.junit.QuarkusTest;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class CamelAppTest extends CamelQuarkusTestSupport {

  @Inject
  CamelContext context;

  @Inject
  ProducerTemplate producerTemplate;


  // Bug
  @Test
  @SneakyThrows
  public void test() {

    //var mock = context.getEndpoint("mock:stream:out", MockEndpoint.class);
    //


    //AdviceWith.adviceWith(context, "orderCreateCommandHandler", r -> {
    //  r.replaceFromWith("direct:orderCreateCommandHandler");
    //  r.mockEndpoints("stream*");
    //});
    //
    //mock.expectedMessageCount(1);
    //mock.expectedBodiesReceived("Hello World");
    //
    //
    //producerTemplate.sendBody("direct:orderCreateCommandHandler", "Talk Talk");
    //var mock = context.getEndpoint("mock:result", MockEndpoint.class);
    // asserting mock is satisfied
    //mock.assertIsSatisfied();

  }

}
