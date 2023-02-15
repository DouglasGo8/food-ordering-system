package com.food.ordering.system.order.service.domain.service;


import io.quarkus.test.junit.QuarkusTest;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class CamelAppIT extends CamelQuarkusTestSupport {

  @Inject
  CamelContext context;

  @Inject
  ProducerTemplate producerTemplate;


  // Bug
  @Test
  @SneakyThrows
  public void test() {

    var mock = this.context.getEndpoint("mock:result", MockEndpoint.class);
    //

    AdviceWith.adviceWith(this.context, "orderCreateRoute", r -> {
      //r.replaceFromWith("direct:orderCreateCommandHandler");
      r.weaveAddLast().to("mock:result");
      //  r.mockEndpoints("stream*");
    });
    //
    mock.expectedMessageCount(1);
    //mock.expectedBodiesReceived("Hello World");
    //
    //
    this.producerTemplate.sendBodyAndHeader("direct:orderCreateCommandHandler",
            "Camel Rocks", "customerId", 1);
    //
    //var mock = context.getEndpoint("mock:result", MockEndpoint.class);
    // asserting mock is satisfied
    mock.assertIsSatisfied();

  }

}
